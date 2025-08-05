package com.esprit.summer.Controller;

import com.esprit.summer.Entities.*;
import com.esprit.summer.Repositories.UserRepo;
import com.esprit.summer.Repositories.UserRoleRepo;
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
}

