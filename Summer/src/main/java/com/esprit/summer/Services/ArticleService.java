package com.esprit.summer.Services;

import com.esprit.summer.Entities.Article;
import com.esprit.summer.Entities.ArticleMovement;
import com.esprit.summer.Entities.Reason;
import com.esprit.summer.Repositories.ArticleMovementRepo;
import com.esprit.summer.Repositories.ArticleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for transactional operations

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
}
