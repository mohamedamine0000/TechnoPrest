package com.esprit.summer.Controller;

import com.esprit.summer.Entities.*;
import com.esprit.summer.Repositories.*;
import com.esprit.summer.ResourceAlreadyExistsException;
import com.esprit.summer.Services.ArticleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepo userRepository;



    @Autowired
    private UserRoleRepo roleRepo;

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private ArticleMovementRepo articleMovementRepo;
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommandeArticleRepo commandeArticleRepo;


    @PostMapping
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Username already exists."));
        }
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Email already exists."));
        }
        if (userRepository.findByCin(registrationDto.getCin()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "CIN already registered."));
        }
        if (registrationDto.getPassword() == null ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Password must be not null"));
        }

        User newUser = new User();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setPassword(registrationDto.getPassword());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setCIN(registrationDto.getCin());



        if (registrationDto.getRoleId() != null) {
            Optional<UserRole> roleOptional = roleRepo.findById(registrationDto.getRoleId());
            if (roleOptional.isPresent()) {
                newUser.setRole(roleOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("message", "Invalid Role ID provided."));
            }
        } else {

        }


        if (registrationDto.getDiplomaProof() != null && !registrationDto.getDiplomaProof().isEmpty()) {
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(registrationDto.getDiplomaProof());
                newUser.setDiplomaProof(decodedBytes);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("message", "Invalid Base64 image data."));
            }
        }

        newUser.setStatus(Status.Waiting);



        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("message", "User registered successfully!"));
    }

    @GetMapping("/{userId}/diploma-proof")
    public ResponseEntity<byte[]> getDiplomaProof(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent() && userOptional.get().getDiplomaProof() != null) {
            byte[] imageData = userOptional.get().getDiplomaProof();
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(imageData);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> loginRequest) {
        String cin = loginRequest.get("cin");
        String password = loginRequest.get("password");

        if (cin == null || password == null || cin.isEmpty() || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", (Object)"CIN and password are required."));
        }

        Optional<User> userOptional = userRepository.findByCin(cin);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // In a real app, you MUST compare hashed passwords using a PasswordEncoder!
            // For demonstration, direct comparison
            if (user.getPassword().equals(password)) {
                // Check user status
                if (user.getStatus() == Status.Accepted) {
                    // Login successful, return user ID
                    return ResponseEntity.ok(Map.of(
                            "message", "Login successful!",
                            "userId", user.getUserId()
                    ));
                } else if (user.getStatus() == Status.Waiting) {
                    // User is in waiting list
                    return ResponseEntity.status(HttpStatus.FORBIDDEN) // Using 403 Forbidden
                            .body(Collections.singletonMap("message", (Object)"You are in the waiting list."));
                } else {
                    // Status is Refused or any other unexpected status
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(Collections.singletonMap("message", (Object)"Your account is not active. Please contact support."));
                }
            } else {
                // Invalid password
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("message", (Object)"Invalid CIN or password."));
            }
        } else {
            // User not found
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("message", (Object)"Invalid CIN or password."));
        }
    }


    // Endpoint to get user details by ID
    @GetMapping("{userId}") // Maps GET requests to /api/users/{userId}
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Map<String, Object> userData = new HashMap<>(); // Use HashMap for mutable map
            userData.put("userId", user.getUserId());
            userData.put("username", user.getUsername());
            userData.put("roleName", user.getRole() != null ? user.getRole().getName() : "N/A");
            userData.put("userCin",user.getCin());
            userData.put("userEmail",user.getEmail());
            userData.put("userStatus",user.getStatus());

            return ResponseEntity.ok(userData); // Return 200 OK with user data
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if user doesn't exist
        }
    }

    @GetMapping("/waiting")
    public ResponseEntity<List<Map<String, Object>>> getWaitingUsers() {
        List<User> waitingUsers = userRepository.findByStatus(Status.Waiting);
        List<Map<String, Object>> userList = waitingUsers.stream()
                .map(user -> {
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("userId", user.getUserId()); // Include userId for frontend actions (accept/deny)
                    userData.put("username", user.getUsername());
                    userData.put("roleName", user.getRole() != null ? user.getRole().getName() : "N/A");
                    userData.put("userCin",user.getCin());
                    userData.put("userEmail",user.getEmail());
                    userData.put("userStatus",user.getStatus());


                    return userData;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(userList);
    }

    @PutMapping("/{userId}/status")
    public ResponseEntity<Map<String, String>> updateUserStatus(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User not found."));
        }

        User user = userOptional.get();
        String statusString = request.get("status"); // Expecting "Accepted" or "Refused"
        if (statusString == null || statusString.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Status field is required."));
        }

        try {
            Status newStatus = Status.valueOf(statusString); // Convert string to Status enum
            user.setStatus(newStatus);
            userRepository.save(user);
            return ResponseEntity.ok(Collections.singletonMap("message", "User status updated successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Invalid status provided. Valid values are Accepted, Waiting, Refused."));
        }
    }

    // NEW ENDPOINT: Delete user
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User not found."));
        }
        userRepository.deleteById(userId);
        return ResponseEntity.ok(Collections.singletonMap("message", "User deleted successfully."));
    }


    @PostMapping("/{userId}/articles")
    public ResponseEntity<Article> addArticle(@PathVariable Long userId, @RequestBody Map<String, Object> articleData) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String reasonString = (String) articleData.get("reason");
        String fromLocation = (String) articleData.get("fromLocation");

        Reason reason;
        try {
            reason = Reason.valueOf(reasonString);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Article article = new Article();
        article.setCodeArticle((String) articleData.get("codeArticle"));
        article.setDesignation((String) articleData.get("designation"));
        article.setUm((String) articleData.get("um"));
        article.setQte((Integer) articleData.get("qte"));
        article.setMinmumStock((Integer) articleData.get("minmumStock"));
        article.setUnite((String) articleData.get("unite"));
        article.setLocation((String) articleData.get("location"));
        article.setEtagere((String) articleData.get("etagere"));
        article.setEtat(com.esprit.summer.Entities.EtatArticle.valueOf((String) articleData.get("etat")));
        article.setDate(new Date());

        // Handle the new fields for sales movement tracking
        String timeframeString = (String) articleData.get("movementTimeframe");
        if (timeframeString != null) {
            try {
                article.setMovementTimeframe(Timeframe.valueOf(timeframeString));
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        if (articleData.containsKey("fastSalesThreshold")) {
            article.setFastSalesThreshold((Integer) articleData.get("fastSalesThreshold"));
        }
        if (articleData.containsKey("mediumSalesThreshold")) {
            article.setMediumSalesThreshold((Integer) articleData.get("mediumSalesThreshold"));
        }


        // --- NEW LOGIC TO HANDLE DATE LIMITS ---
        if (articleData.get("datelimitNumber") != null) {
            article.setDatelimitNumber(((Number) articleData.get("datelimitNumber")).intValue());
        }

        if (articleData.get("datelimitUnit") != null) {
            try {
                String unitString = (String) articleData.get("datelimitUnit");
                article.setDatelimitUnit(TimeUnit.valueOf(unitString));
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }

        if (articleData.get("alertBeforeDatelimitNumber") != null) {
            article.setAlertBeforeDatelimitNumber(((Number) articleData.get("alertBeforeDatelimitNumber")).intValue());
        }

        if (articleData.get("alertBeforeDatelimitUnit") != null) {
            try {
                String unitString = (String) articleData.get("alertBeforeDatelimitUnit");
                article.setAlertBeforeDatelimitUnit(TimeUnit.valueOf(unitString));
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        // ---------------------------------------



        Map<String, Object> roleData = (Map<String, Object>) articleData.get("role");
        if (roleData == null || roleData.get("id") == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long roleId = ((Number) roleData.get("id")).longValue();
        Optional<UserRole> roleOptional = roleRepo.findById(roleId);
        if (roleOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        article.setRole(roleOptional.get());

        if (articleData.containsKey("reservedByWho")) {
            article.setReservedByWho((String) articleData.get("reservedByWho"));
        }
        if (articleData.containsKey("reservedToWho")) {
            article.setReservedToWho((String) articleData.get("reservedToWho"));
        }

        User user = userOptional.get();
        String addedBy = user.getCin();

        try {
            Article newArticle = articleService.addArticle(article, addedBy, reason, fromLocation);
            return new ResponseEntity<>(newArticle, HttpStatus.CREATED);
        } catch (ResourceAlreadyExistsException e) {
            // Return a 409 Conflict status with the updated, more concise message
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    // API to get an article by ID
    @GetMapping("/articles/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Optional<Article> article = articleService.getArticleById(id);
        return article.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // API to get all articles
    @GetMapping("/articles")
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    // API to update an article's quantity
    @PutMapping("/articles/{id}/quantity")
    public ResponseEntity<Article> updateArticleQuantity(@PathVariable Long id, @RequestBody Map<String, Object> updateRequest) {
        try {
            Integer quantityChange = (Integer) updateRequest.get("quantityChange");
            String reasonString = (String) updateRequest.get("reason");
            String recordedBy = "system"; // Replace with actual user

            if (quantityChange == null || reasonString == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Reason reason = Reason.valueOf(reasonString);
            Optional<Article> updatedArticle = articleService.updateArticleQuantity(id, quantityChange, reason, recordedBy);

            return updatedArticle.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // API to change an article's location
    @PutMapping("/articles/{id}/location")
    public ResponseEntity<Article> changeArticleLocation(@PathVariable Long id, @RequestBody Map<String, String> updateRequest) {
        String newLocation = updateRequest.get("newLocation");
        String recordedBy = "depot_manager"; // Replace with actual user

        if (newLocation == null || newLocation.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Article> updatedArticle = articleService.changeArticleLocation(id, newLocation, recordedBy);

        return updatedArticle.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/articles/{articleId}")
    @Transactional
    public ResponseEntity<?> deleteArticle(@PathVariable Long articleId) {
        try {
            // First delete all related movements
            articleMovementRepo.deleteAllByArticleId(articleId);

            // Then delete the article
            articleRepo.deleteById(articleId);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting article: " + e.getMessage());
        }
    }

    @PutMapping("/{articleId}/sell")
    public ResponseEntity<?> sellArticle(@PathVariable Long articleId, @RequestBody Map<String, Object> payload) {
        try {
            Number quantityNumber = (Number) payload.get("quantityChange");
            if (quantityNumber == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity is required.");
            }
            int quantityToSell = quantityNumber.intValue();
            String recordedBy = (String) payload.get("recordedBy");

            // Extract the new fields from the payload
            String client = (String) payload.get("client");
            String clientZone = (String) payload.get("clientZone");
            String clientActivityDomain = (String) payload.get("clientActivityDomain");
            String priseEnCharge = (String) payload.get("priseEnCharge");

            // Call the service method with all the new parameters
            articleService.sellArticle(articleId, quantityToSell, recordedBy, client, clientZone, clientActivityDomain, priseEnCharge);
            return ResponseEntity.ok("Article quantity updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while selling the article.");
        }
    }
    @PutMapping("/articles/{articleId}/change-depo")
    public ResponseEntity<?> changeDepoAndReduceQuantity(@PathVariable Long articleId, @RequestBody Map<String, Object> payload) {
        try {
            Number quantityNumber = (Number) payload.get("quantityChange");
            if (quantityNumber == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity is required.");
            }
            int quantityToChange = quantityNumber.intValue();
            String newLocation = (String) payload.get("newLocation");
            String recordedBy = (String) payload.get("recordedBy");

            if (newLocation == null || newLocation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New location is required.");
            }

            articleService.changeDepoAndReduceQuantity(articleId, quantityToChange, newLocation, recordedBy);
            return ResponseEntity.ok("Article quantity and location updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the article.");
        }
    }

    @PutMapping("/articles/{articleId}/add-from-depo")
    public ResponseEntity<?> addFromDepoAndIncreaseQuantity(@PathVariable Long articleId, @RequestBody Map<String, Object> payload) {
        try {
            Number quantityNumber = (Number) payload.get("quantityChange");
            if (quantityNumber == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity is required.");
            }
            int quantityToAdd = quantityNumber.intValue();
            String newLocation = (String) payload.get("newLocation");
            String recordedBy = (String) payload.get("recordedBy");

            if (newLocation == null || newLocation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("New location is required.");
            }

            articleService.addFromDepoAndIncreaseQuantity(articleId, quantityToAdd, newLocation, recordedBy);
            return ResponseEntity.ok("Article quantity and location updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the article.");
        }
    }


    @GetMapping("/articles/{articleId}/movements")
    public ResponseEntity<List<ArticleMovement>> getArticleMovements(@PathVariable Long articleId) {
        Optional<Article> articleOptional = articleRepo.findById(articleId);
        if (articleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Article article = articleOptional.get();
        List<ArticleMovement> movements = articleMovementRepo.findByArticle(article);
        return ResponseEntity.ok(movements);
    }
    @GetMapping("/articles/{articleId}/distribution/movements")
    public ResponseEntity<Map<String, Integer>> getArticleMovementDistribution(@PathVariable Long articleId) {
        Optional<Article> articleOptional = articleRepo.findById(articleId);
        if (articleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Article article = articleOptional.get();
        List<ArticleMovement> movements = articleMovementRepo.findByArticle(article);

        Map<String, Integer> distribution = new HashMap<>();
        for (ArticleMovement movement : movements) {
            // Only consider sales movements (negative quantity change)
            if (movement.getQuantityChange() < 0) {
                String clientZone = movement.getClientZone();
                // Add a null and empty string check
                if (clientZone != null && !clientZone.trim().isEmpty()) {
                    int quantity = Math.abs(movement.getQuantityChange());
                    distribution.put(clientZone, distribution.getOrDefault(clientZone, 0) + quantity);
                }
            }
        }

        return ResponseEntity.ok(distribution);
    }
    @GetMapping("/articles/distribution/location")
    public ResponseEntity<Map<String, Integer>> getArticleDistributionByLocation() {
        List<Article> allArticles = articleRepo.findAll();
        Map<String, Integer> distribution = new HashMap<>();

        for (Article article : allArticles) {
            String location = article.getLocation();
            distribution.put(location, distribution.getOrDefault(location, 0) + article.getQte());
        }

        return ResponseEntity.ok(distribution);
    }

    // API to update an article
    @PutMapping("/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article updatedArticle) {
        Optional<Article> articleOptional = articleRepo.findById(id);
        if (articleOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Article existingArticle = articleOptional.get();

        // Update the fields that can be modified
        existingArticle.setCodeArticle(updatedArticle.getCodeArticle());
        existingArticle.setDesignation(updatedArticle.getDesignation());
        existingArticle.setUm(updatedArticle.getUm());
        existingArticle.setQte(updatedArticle.getQte());
        existingArticle.setMinmumStock(updatedArticle.getMinmumStock());
        existingArticle.setUnite(updatedArticle.getUnite());
        existingArticle.setLocation(updatedArticle.getLocation());
        existingArticle.setEtagere(updatedArticle.getEtagere());
        existingArticle.setEtat(updatedArticle.getEtat());
        existingArticle.setMovementTimeframe(updatedArticle.getMovementTimeframe());
        existingArticle.setFastSalesThreshold(updatedArticle.getFastSalesThreshold());
        existingArticle.setMediumSalesThreshold(updatedArticle.getMediumSalesThreshold());
        existingArticle.setReservedByWho(updatedArticle.getReservedByWho());
        existingArticle.setReservedToWho(updatedArticle.getReservedToWho());

        // CRITICAL FIX: The date and role fields were not being updated.
        existingArticle.setDate(updatedArticle.getDate());
        existingArticle.setRole(updatedArticle.getRole());

        existingArticle.setDatelimitNumber(updatedArticle.getDatelimitNumber());
        existingArticle.setDatelimitUnit(updatedArticle.getDatelimitUnit());
        existingArticle.setAlertBeforeDatelimitNumber(updatedArticle.getAlertBeforeDatelimitNumber());
        existingArticle.setAlertBeforeDatelimitUnit(updatedArticle.getAlertBeforeDatelimitUnit());

        Article savedArticle = articleRepo.save(existingArticle);
        return new ResponseEntity<>(savedArticle, HttpStatus.OK);
    }

    // API to get low stock articles for the logged-in user's role
    @GetMapping("/articles/low-stock/{userId}")
    public ResponseEntity<List<Article>> getLowStockArticlesForUserRole(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty() || userOptional.get().getRole() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        UserRole userRole = userOptional.get().getRole();
        List<Article> articles = articleService.getLowStockArticlesByRole(userRole);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    // API to get all low stock articles (for admin)
    @GetMapping("/articles/low-stock/all")
    public ResponseEntity<List<Article>> getAllLowStockArticles() {
        List<Article> articles = articleService.getLowStockArticlesForAllRoles();
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }


    @PutMapping("/articles/{articleId}/CommandeEnCours")
    public ResponseEntity<?> CommandeEnCours(@PathVariable Long articleId, @RequestBody Map<String, Object> payload) {
        try {
            Number quantityNumber = (Number) payload.get("quantityChange");
            if (quantityNumber == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity is required.");
            }
            int quantityToAdd = quantityNumber.intValue();
            String reservedBy = (String) payload.get("reservedBy");
            String reservedTo= (String) payload.get("reservedTo");
            String recordedBy = (String) payload.get("recordedBy");

            

            articleService.CommandeEnCours(articleId, quantityToAdd, reservedTo,reservedBy, recordedBy);
            return ResponseEntity.ok("Article Commande En cours :) ");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

    } catch (Exception e) {
        e.printStackTrace(); // 👈 add this so you can see the root cause in your console
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
    }
    }


    @GetMapping("/articles/CommandeEnCours/all")
    public ResponseEntity<List<ArticleMovement>> getAllCommandeEnCours() {
        List<ArticleMovement> articlesM = articleService.ListCommandeEnCours();
        return new ResponseEntity<>(articlesM, HttpStatus.OK);
    }


    @GetMapping("/articles/CommandeEnCours/byUser/{userId}")
    public ResponseEntity<List<ArticleMovement>> getCommandeEnCoursByUser(@PathVariable Long userId) {
        List<ArticleMovement> movements = articleService.getCommandeEnCoursForUser(userId);
        return new ResponseEntity<>(movements, HttpStatus.OK);
    }

    @PostMapping("/reserved-articles/add-and-delete")
    public ResponseEntity<CommandeArticleMV> addReservedArticleAndRemoveMovement(@RequestBody CommandeArticleMV reservedArticle, @RequestParam Long movementIdToDelete) {
        CommandeArticleMV result = articleService.addReservedArticleAndDeleteMovement(reservedArticle, movementIdToDelete);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/articles/{articleId}/add-from-bought")
    public ResponseEntity<?> addFromBoughtAndIncreaseQuantity(@PathVariable Long articleId, @RequestBody Map<String, Object> payload) {
        try {
            Number quantityNumber = (Number) payload.get("quantityChange");
            if (quantityNumber == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity is required.");
            }
            int quantityToAdd = quantityNumber.intValue();
            String newLocation = (String) payload.get("newLocation");
            String recordedBy = (String) payload.get("recordedBy");



            articleService.addFromBoughtAndIncreaseQuantity(articleId, quantityToAdd, newLocation, recordedBy);
            return ResponseEntity.ok("Article quantity updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the article.");
        }
    }

    @DeleteMapping("/articles/movements/{movementIdToDelete}")
    public ResponseEntity<String> deleteMovement(@PathVariable Long movementIdToDelete) {
        try {
            articleService.DeleteMovement(movementIdToDelete);
            return ResponseEntity.ok("Article movement deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting article movement: " + e.getMessage());
        }
    }



    @GetMapping("/expired-or-alert")
    public ResponseEntity<List<ArticleBatch>> getExpiredOrAlertBatchesForAdmin() {
        List<ArticleBatch> batch = articleService.getExpiredOrAlertBatchesForAdmin();
        return new ResponseEntity<>(batch, HttpStatus.OK);
    }
    @GetMapping("/expired-or-alert/role/{roleName}")
    public ResponseEntity<List<ArticleBatch>> getExpiredAlertsByRole(@PathVariable String roleName) {
        List<ArticleBatch> batches = articleService.getExpiredOrAlertBatchesForUser(roleName);
        return new ResponseEntity<>(batches, HttpStatus.OK);
    }



    @PostMapping("/reduce-by-batch/{batchId}")
    public ResponseEntity<Void> reduceArticleQuantityByBatch(
            @PathVariable Long batchId,
            @RequestBody String recordedBy
    ) {
        try {
            articleService.reduceQuantityByBatch(batchId, recordedBy);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/history/movements/{userId}")
    public ResponseEntity<List<ArticleMovement>> getArticleMovementHistory(@PathVariable Long userId) {
        List<ArticleMovement> movements = articleService.getMovementHistory(userId);
        return ResponseEntity.ok(movements);
    }



}