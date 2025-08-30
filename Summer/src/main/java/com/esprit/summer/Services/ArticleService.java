package com.esprit.summer.Services;

import com.esprit.summer.Entities.*;
import com.esprit.summer.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private ArticleMovementRepo articleMovementRepo;

    @Autowired
    private UserRoleRepo userRoleRepo;

    @Autowired
    private CommandeArticleRepo commandeArticleRepo;

    @Autowired
    private ArticleBatchRepository articleBatchRepo;

    // Method to add a new article (initial stock)
    @Transactional
    public Article addArticle(Article article, String addedBy, Reason reason, String fromLocation) {
        article.setAddedBy(addedBy);
        Article savedArticle = articleRepo.save(article);

        ArticleMovement initialMovement = ArticleMovement.builder()
                .article(savedArticle)
                .quantityChange(savedArticle.getQte())
                .reason(reason)
                .fromLocation(fromLocation)
                .toLocation(savedArticle.getLocation())
                .timestamp(LocalDateTime.now())
                .recordedBy(addedBy)
                .build();
        articleMovementRepo.save(initialMovement);

        // NEW LOGIC: Check if the article has a date limit and create an ArticleBatch
        if (savedArticle.getDatelimitNumber() != null && savedArticle.getDatelimitUnit() != null) {
            LocalDate purchaseDate = LocalDate.now();

            LocalDate expiryDate = calculateDate(
                    purchaseDate,
                    savedArticle.getDatelimitNumber(),
                    savedArticle.getDatelimitUnit()
            );

            LocalDate expiryAlert = null;
            if (savedArticle.getAlertBeforeDatelimitNumber() != null && savedArticle.getAlertBeforeDatelimitUnit() != null) {
                expiryAlert = calculateDate(
                        expiryDate,
                        -savedArticle.getAlertBeforeDatelimitNumber(),
                        savedArticle.getAlertBeforeDatelimitUnit()
                );
            }

            ArticleBatch batch = ArticleBatch.builder()
                    .article(savedArticle)
                    .quantity(savedArticle.getQte())
                    .purchaseDate(purchaseDate)
                    .expiryDate(expiryDate)
                    .expiryAlert(expiryAlert)
                    .build();

            articleBatchRepo.save(batch);
        }



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

    @Transactional
    public Optional<Article> sellArticle(Long articleId, int quantityToSell, String recordedBy, String client, String clientZone, String clientActivityDomain, String priseEnCharge) {
        Optional<Article> articleOptional = articleRepo.findById(articleId);
        if (articleOptional.isEmpty()) {
            throw new IllegalArgumentException("Article not found with ID: " + articleId);
        }

        Article article = articleOptional.get();
        int currentQuantity = article.getQte();

        // Check for invalid quantity at the start
        if (quantityToSell <= 0 || quantityToSell > currentQuantity) {
            throw new IllegalArgumentException("Invalid quantity for sale.");
        }

        // 1. Fetch all reservations for this article
        List<CommandeArticleMV> reservations = commandeArticleRepo.findByArticle_ArticleId(articleId);

        // 2. Find the total quantity reserved across all reservations
        int totalReservedQuantity = reservations.stream()
                .mapToInt(CommandeArticleMV::getQuantity)
                .sum();

        // 3. Check if the client matches any reservation
        List<CommandeArticleMV> matchingReservations = reservations.stream()
                .filter(res -> res.getReservedTo().equals(client))
                .collect(Collectors.toList());

        if (!matchingReservations.isEmpty()) {
            // Case 1: Selling to a reserved client
            int soldFromReservation = 0;
            int remainingToSell = quantityToSell;

            // Iterate through the matching reservations and fulfill the sale
            for (CommandeArticleMV reservation : matchingReservations) {
                if (remainingToSell <= 0) break;

                int quantityToFulfill = Math.min(remainingToSell, reservation.getQuantity());

                reservation.setQuantity(reservation.getQuantity() - quantityToFulfill);
                remainingToSell -= quantityToFulfill;

                if (reservation.getQuantity() <= 0) {
                    commandeArticleRepo.delete(reservation);
                } else {
                    commandeArticleRepo.save(reservation);
                }

                soldFromReservation += quantityToFulfill;
            }


        } else {
            // Case 2: Selling to a non-reserved client
            // Check if selling this quantity would violate the overall reservation constraint
            int availableForSale = currentQuantity - totalReservedQuantity;


            if (quantityToSell > availableForSale) {
                String reservedClients = reservations.stream()
                        .map(CommandeArticleMV::getReservedTo)
                        .distinct()
                        .collect(Collectors.joining(", "));

                String errorMessage = "Not enough unreserved stock available. The following clients have a total of " +
                        totalReservedQuantity + " units reserved: " + reservedClients + ".";

                throw new IllegalArgumentException(errorMessage);
            }
        }

        // 4. Perform the stock reduction and movement record
        int newQuantity = currentQuantity - quantityToSell;
        article.setQte(newQuantity);
        Article updatedArticle = articleRepo.save(article);

        // Record the movement with all the new fields
        ArticleMovement movement = ArticleMovement.builder()
                .article(updatedArticle)
                .quantityChange(-quantityToSell)
                .reason(Reason.Sold)
                .fromLocation(updatedArticle.getLocation())
                .toLocation(updatedArticle.getLocation())
                .timestamp(LocalDateTime.now())
                .recordedBy(recordedBy)
                .client(client)
                .clientZone(clientZone)
                .clientActivityDomain(clientActivityDomain)
                .priseEnCharge(priseEnCharge)
                .build();
        articleMovementRepo.save(movement);

        return Optional.of(updatedArticle);
    }
    @Transactional
    public Optional<Article> changeDepoAndReduceQuantity(Long articleId, int quantityToChange, String newLocation, String recordedBy) {
        Optional<Article> articleOptional = articleRepo.findById(articleId);
        if (articleOptional.isEmpty()) {
            throw new IllegalArgumentException("Article not found with ID: " + articleId);
        }

        Article article = articleOptional.get();
        int currentQuantity = article.getQte();

        if (quantityToChange <= 0 || quantityToChange > currentQuantity) {
            throw new IllegalArgumentException("Invalid quantity for depot change.");
        }

        // Capture the original location before updating quantity
        String oldLocation = article.getLocation();

        // Reduce the quantity of the existing article
        int newQuantity = currentQuantity - quantityToChange;
        article.setQte(newQuantity);
        Article updatedArticle = articleRepo.save(article);

        // Record a single movement for the depot change
        ArticleMovement movement = ArticleMovement.builder()
                .article(updatedArticle)
                .quantityChange(-quantityToChange) // Quantity is negative as it is leaving this location
                .reason(Reason.ChangeDepo)
                .fromLocation(oldLocation)
                .toLocation(newLocation) // New location is recorded here
                .timestamp(LocalDateTime.now())
                .recordedBy(recordedBy)
                .build();
        articleMovementRepo.save(movement);

        return Optional.of(updatedArticle);
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

        // NEW LOGIC: Create a new ArticleBatch if the article has a date limit
        if (article.getDatelimitNumber() != null && article.getDatelimitUnit() != null) {
            LocalDate purchaseDate = LocalDate.now();

            LocalDate expiryDate = calculateDate(
                    purchaseDate,
                    article.getDatelimitNumber(),
                    article.getDatelimitUnit()
            );

            LocalDate expiryAlert = null;
            if (article.getAlertBeforeDatelimitNumber() != null && article.getAlertBeforeDatelimitUnit() != null) {
                expiryAlert = calculateDate(
                        expiryDate,
                        -article.getAlertBeforeDatelimitNumber(),
                        article.getAlertBeforeDatelimitUnit()
                );
            }

            ArticleBatch batch = ArticleBatch.builder()
                    .article(article)
                    .quantity(quantityToAdd) // Use the quantity being added, not the total quantity
                    .purchaseDate(purchaseDate)
                    .expiryDate(expiryDate)
                    .expiryAlert(expiryAlert)
                    .build();

            articleBatchRepo.save(batch);
        }

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

    public List<Article> getLowStockArticlesByRole(UserRole userRole) {
        return articleRepo.findLowStockByRole(userRole);
    }

    public List<Article> getLowStockArticlesForAllRoles() {
        return articleRepo.findAllLowStock();
    }

    @Transactional
    public void CommandeEnCours(Long articleId, int quantityToAdd, String reservedTo, String reservedBy, String recordedBy) {
        Optional<Article> articleOptional = articleRepo.findById(articleId);
        if (articleOptional.isEmpty()) {
            throw new IllegalArgumentException("Article not found with ID: " + articleId);
        }

        Article article = articleOptional.get();

        ArticleMovement movement = ArticleMovement.builder()
                .article(article)
                .quantityChange(quantityToAdd)
                .reason(Reason.CommandeEnCours)
                .reservedTo(reservedTo)
                .reservedBy(reservedBy)
                .fromLocation(article.getLocation() != null ? article.getLocation() : "")
                .toLocation(article.getLocation() != null ? article.getLocation() : "")
                .recordedBy(recordedBy)
                .timestamp(LocalDateTime.now())
            
                .build();
        articleMovementRepo.save(movement);
    }


   public List<ArticleMovement> ListCommandeEnCours() { return articleMovementRepo.findByReason(Reason.CommandeEnCours);}



    public List<ArticleMovement> getCommandeEnCoursForUser(Long userId) {
        return articleMovementRepo.findByReasonAndUserId(Reason.CommandeEnCours, userId);
    }


    @Transactional
    public CommandeArticleMV addReservedArticleAndDeleteMovement(CommandeArticleMV reservedArticle, Long movementIdToDelete) {
        CommandeArticleMV savedReservedArticle = commandeArticleRepo.save(reservedArticle);
        articleMovementRepo.deleteById(movementIdToDelete);
        return savedReservedArticle;
    }

    @Transactional
    public void addFromBoughtAndIncreaseQuantity(Long articleId, int quantityToAdd, String fromLocation, String recordedBy) {
        Optional<Article> articleOptional = articleRepo.findById(articleId);
        if (articleOptional.isEmpty()) {
            throw new IllegalArgumentException("Article not found with ID: " + articleId);
        }

        Article article = articleOptional.get();

        int newQuantity = article.getQte() + quantityToAdd;
        article.setQte(newQuantity);

        articleRepo.save(article);
        // NEW LOGIC: Create a new ArticleBatch if the article has a date limit
        if (article.getDatelimitNumber() != null && article.getDatelimitUnit() != null) {
            LocalDate purchaseDate = LocalDate.now();

            LocalDate expiryDate = calculateDate(
                    purchaseDate,
                    article.getDatelimitNumber(),
                    article.getDatelimitUnit()
            );

            LocalDate expiryAlert = null;
            if (article.getAlertBeforeDatelimitNumber() != null && article.getAlertBeforeDatelimitUnit() != null) {
                expiryAlert = calculateDate(
                        expiryDate,
                        -article.getAlertBeforeDatelimitNumber(),
                        article.getAlertBeforeDatelimitUnit()
                );
            }

            ArticleBatch batch = ArticleBatch.builder()
                    .article(article)
                    .quantity(quantityToAdd) // Use the quantity being added
                    .purchaseDate(purchaseDate)
                    .expiryDate(expiryDate)
                    .expiryAlert(expiryAlert)
                    .build();

            articleBatchRepo.save(batch);
        }
        ArticleMovement movement = ArticleMovement.builder()
                .article(article)
                .quantityChange(quantityToAdd)
                .reason(Reason.Bought)
                .fromLocation(fromLocation)
                .toLocation(article.getLocation())
                .recordedBy(recordedBy)
                .timestamp(LocalDateTime.now())
                .build();
        articleMovementRepo.save(movement);
    }

    @Transactional
    public void DeleteMovement(Long movementIdToDelete) {
        articleMovementRepo.deleteById(movementIdToDelete);
    }


    private LocalDate calculateDate(LocalDate baseDate, Integer number, TimeUnit unit) {
        if (number == null || unit == null) {
            return null;
        }
        switch (unit) {
            case DAYS:
                return baseDate.plusDays(number);
            case WEEKS:
                return baseDate.plusWeeks(number);
            case MONTHS:
                return baseDate.plusMonths(number);
            case YEARS:
                return baseDate.plusYears(number);
            default:
                return baseDate;
        }
    }
    public List<ArticleBatch> getExpiredOrAlertBatchesForAdmin() {
        return articleBatchRepo.findExpiredOrAlertBatches(LocalDate.now());
    }

    public List<ArticleBatch> getExpiredOrAlertBatchesForUser(String roleName) {
        Optional<UserRole> userRoleOptional = userRoleRepo.findByName(roleName);

        if (userRoleOptional.isPresent()) {
            return articleBatchRepo.findExpiredOrAlertBatchesByRole(userRoleOptional.get(), LocalDate.now());
        } else {
            // Return an empty list if the role name is not found
            return Collections.emptyList();
        }
    }


    @Transactional
    public void reduceQuantityByBatch(Long batchId, String recordedBy) {
        Optional<ArticleBatch> batchOptional = articleBatchRepo.findById(batchId);
        if (batchOptional.isPresent()) {
            ArticleBatch batch = batchOptional.get();
            Article article = batch.getArticle();

            int quantityToRemove = batch.getQuantity();

            if (quantityToRemove > article.getQte()) {
                quantityToRemove = article.getQte();
            }

            int newQuantity = article.getQte() - quantityToRemove;
            article.setQte(newQuantity);
            articleRepo.save(article);

            ArticleMovement movement = ArticleMovement.builder()
                    .article(article)
                    .quantityChange(-quantityToRemove)
                    .reason(Reason.Poubelle)
                    .fromLocation(article.getLocation())
                    .toLocation(null)
                    .recordedBy(recordedBy)
                    .timestamp(LocalDateTime.now())
                    .build();
            articleMovementRepo.save(movement);
            articleBatchRepo.delete(batch);
        } else {
            throw new IllegalArgumentException("Article batch not found with ID: " + batchId);
        }
    }
}