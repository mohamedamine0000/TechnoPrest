package com.esprit.summer.Services;

import com.esprit.summer.Entities.Article;
import com.esprit.summer.Entities.ArticleMovement;
import com.esprit.summer.Entities.Reason;
import com.esprit.summer.Repositories.ArticleMovementRepo;
import com.esprit.summer.Repositories.ArticleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private ArticleMovementRepo articleMovementRepo;

    // Method to add a new article (initial stock)
    @Transactional
    public Article addArticle(Article article, String addedBy, Reason reason, String fromLocation) {
        article.setAddedBy(addedBy);
        // Save the article first
        Article savedArticle = articleRepo.save(article);

        // Record the initial purchase/addition movement
        ArticleMovement initialMovement = ArticleMovement.builder()
                .article(savedArticle)
                .quantityChange(savedArticle.getQte()) // Initial quantity is the change
                .reason(reason) // Use the reason provided in the request
                .fromLocation(fromLocation) // Use the fromLocation provided in the request
                .toLocation(savedArticle.getLocation())
                .timestamp(LocalDateTime.now())
                .recordedBy(addedBy)
                .build();
        articleMovementRepo.save(initialMovement);

        return savedArticle;
    }

    // Method to update an article's quantity (e.g., after a sale or purchase)
    @Transactional
    public Optional<Article> updateArticleQuantity(Long articleId, int quantityChange, Reason reason, String recordedBy) {
        Optional<Article> articleOptional = articleRepo.findById(articleId);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            int oldQuantity = article.getQte();
            int newQuantity = oldQuantity + quantityChange; // quantityChange can be positive (bought) or negative (sold)

            if (newQuantity < 0) {
                // Handle insufficient stock error, perhaps throw a custom exception
                throw new IllegalArgumentException("Insufficient stock for article " + article.getDesignation());
            }

            article.setQte(newQuantity);
            Article updatedArticle = articleRepo.save(article);

            // Record the movement
            ArticleMovement movement = ArticleMovement.builder()
                    .article(updatedArticle)
                    .quantityChange(quantityChange)
                    .reason(reason)
                    .fromLocation(updatedArticle.getLocation()) // Location remains the same for sale/purchase
                    .toLocation(updatedArticle.getLocation())
                    .timestamp(LocalDateTime.now())
                    .recordedBy(recordedBy)
                    .build();
            articleMovementRepo.save(movement);

            return Optional.of(updatedArticle);
        }
        return Optional.empty();
    }

    // Method to change an article's location (depot change)
    @Transactional
    public Optional<Article> changeArticleLocation(Long articleId, String newLocation, String recordedBy) {
        Optional<Article> articleOptional = articleRepo.findById(articleId);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            String oldLocation = article.getLocation();
            article.setLocation(newLocation);
            Article updatedArticle = articleRepo.save(article);

            // Record the depot change movement
            ArticleMovement movement = ArticleMovement.builder()
                    .article(updatedArticle)
                    .quantityChange(0) // Quantity doesn't change for a simple depot transfer
                    .reason(Reason.ChangeDepo)
                    .fromLocation(oldLocation)
                    .toLocation(newLocation)
                    .timestamp(LocalDateTime.now())
                    .recordedBy(recordedBy)
                    .build();
            articleMovementRepo.save(movement);

            return Optional.of(updatedArticle);
        }
        return Optional.empty();
    }

    // Method to delete an article (also record a movement, perhaps 'Disposed' or 'Removed')
    @Transactional
    public boolean deleteArticle(Long articleId, String recordedBy) {
        Optional<Article> articleOptional = articleRepo.findById(articleId);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();

            // Record a final movement indicating removal/disposal
            ArticleMovement finalMovement = ArticleMovement.builder()
                    .article(article)
                    .quantityChange(-article.getQte()) // Reduce current stock to 0
                    .reason(Reason.Sold) // Or a new enum value like Reason.Disposed
                    .fromLocation(article.getLocation())
                    .toLocation(null) // No destination
                    .timestamp(LocalDateTime.now())
                    .recordedBy(recordedBy)
                    .build();
            articleMovementRepo.save(finalMovement);

            articleRepo.deleteById(articleId);
            return true;
        }
        return false;
    }

    // Other standard CRUD methods for Article
    public Optional<Article> getArticleById(Long articleId) {
        return articleRepo.findById(articleId);
    }

    public List<Article> getAllArticles() {
        return articleRepo.findAll();
    }

    // NEW METHOD FOR SELLING ARTICLES
    @Transactional
    public Optional<Article> sellArticle(Long articleId, int quantityToSell, String recordedBy) {
        Optional<Article> articleOptional = articleRepo.findById(articleId);
        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();
            int currentQuantity = article.getQte();

            if (quantityToSell <= 0 || quantityToSell > currentQuantity) {
                throw new IllegalArgumentException("Invalid quantity for sale.");
            }

            int newQuantity = currentQuantity - quantityToSell;
            article.setQte(newQuantity);
            Article updatedArticle = articleRepo.save(article);

            // Record the movement with the correct recordedBy and reason
            ArticleMovement movement = ArticleMovement.builder()
                    .article(updatedArticle)
                    .quantityChange(-quantityToSell)
                    .reason(Reason.Sold) // Explicitly set the reason to Sold
                    .fromLocation(updatedArticle.getLocation())
                    .toLocation(updatedArticle.getLocation())
                    .timestamp(LocalDateTime.now())
                    .recordedBy(recordedBy) // This will be the user's CIN
                    .build();
            articleMovementRepo.save(movement);

            return Optional.of(updatedArticle);
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Article> changeDepoAndReduceQuantity(Long articleId, int quantityToChange, String newLocation, String recordedBy) {
        Optional<Article> sourceArticleOptional = articleRepo.findById(articleId);
        if (sourceArticleOptional.isEmpty()) {
            throw new IllegalArgumentException("Source article not found with ID: " + articleId);
        }

        Article sourceArticle = sourceArticleOptional.get();
        int currentQuantity = sourceArticle.getQte();

        if (quantityToChange <= 0 || quantityToChange > currentQuantity) {
            throw new IllegalArgumentException("Invalid quantity for depot change.");
        }

        String oldLocation = sourceArticle.getLocation();

        // 1. Reduce quantity from the source article
        int newSourceQuantity = currentQuantity - quantityToChange;
        sourceArticle.setQte(newSourceQuantity);
        articleRepo.save(sourceArticle);

        // 2. Find or create the destination article
        Optional<Article> destinationArticleOptional = articleRepo.findByCodeArticleAndLocation(sourceArticle.getCodeArticle(), newLocation);

        if (destinationArticleOptional.isPresent()) {
            // If the article already exists at the destination, just increase its quantity
            Article destinationArticle = destinationArticleOptional.get();
            destinationArticle.setQte(destinationArticle.getQte() + quantityToChange);
            articleRepo.save(destinationArticle);

            // Record the movement for the addition to the destination
            ArticleMovement destinationMovement = ArticleMovement.builder()
                    .article(destinationArticle)
                    .quantityChange(quantityToChange)
                    .reason(Reason.ChangeDepo)
                    .fromLocation(oldLocation)
                    .toLocation(newLocation)
                    .timestamp(LocalDateTime.now())
                    .recordedBy(recordedBy)
                    .build();
            articleMovementRepo.save(destinationMovement);

        } else {
            // If the article does not exist at the destination, create a new one
            Article newArticle = new Article();
            newArticle.setCodeArticle(sourceArticle.getCodeArticle());
            newArticle.setDesignation(sourceArticle.getDesignation() + " (" + newLocation + ")");
            newArticle.setUm(sourceArticle.getUm());
            newArticle.setQte(quantityToChange);
            newArticle.setMinmumStock(sourceArticle.getMinmumStock());
            newArticle.setUnite(sourceArticle.getUnite());
            newArticle.setLocation(newLocation);
            newArticle.setEtagere(sourceArticle.getEtagere());
            newArticle.setEtat(sourceArticle.getEtat());
            newArticle.setDate(sourceArticle.getDate());
            newArticle.setMovementTimeframe(sourceArticle.getMovementTimeframe());
            newArticle.setFastSalesThreshold(sourceArticle.getFastSalesThreshold());
            newArticle.setMediumSalesThreshold(sourceArticle.getMediumSalesThreshold());
            newArticle.setRole(sourceArticle.getRole());
            newArticle.setReservedByWho(sourceArticle.getReservedByWho());
            newArticle.setReservedToWho(sourceArticle.getReservedToWho());
            newArticle.setAddedBy(sourceArticle.getAddedBy());

            Article savedNewArticle = articleRepo.save(newArticle);

            // Record the movement for the creation of the new article
            ArticleMovement creationMovement = ArticleMovement.builder()
                    .article(savedNewArticle)
                    .quantityChange(quantityToChange)
                    .reason(Reason.ChangeDepo)
                    .fromLocation(oldLocation)
                    .toLocation(newLocation)
                    .timestamp(LocalDateTime.now())
                    .recordedBy(recordedBy)
                    .build();
            articleMovementRepo.save(creationMovement);
        }

        // Record the movement for the reduction from the source article
        ArticleMovement sourceMovement = ArticleMovement.builder()
                .article(sourceArticle)
                .quantityChange(-quantityToChange)
                .reason(Reason.ChangeDepo)
                .fromLocation(oldLocation)
                .toLocation(newLocation)
                .timestamp(LocalDateTime.now())
                .recordedBy(recordedBy)
                .build();
        articleMovementRepo.save(sourceMovement);

        return Optional.of(sourceArticle);
    }

    @Transactional
    public void addFromDepoAndIncreaseQuantity(Long articleId, int quantityToAdd, String fromLocation, String recordedBy) {
        Optional<Article> articleOptional = articleRepo.findById(articleId);
        if (articleOptional.isEmpty()) {
            throw new IllegalArgumentException("Article not found with ID: " + articleId);
        }

        Article article = articleOptional.get();

        // Increase the quantity of the existing article.
        int newQuantity = article.getQte() + quantityToAdd;
        article.setQte(newQuantity);

        // Save the updated article. Note: The article's location is NOT changed here.
        articleRepo.save(article);

        // Create and save an ArticleMovement entry with the correct locations.
        ArticleMovement movement = ArticleMovement.builder()
                .article(article)
                .quantityChange(quantityToAdd)
                .reason(Reason.ChangeDepo)
                .fromLocation(fromLocation)
                .toLocation(article.getLocation())
                .recordedBy(recordedBy)
                .timestamp(LocalDateTime.now())
                .build();
        articleMovementRepo.save(movement);
    }
}