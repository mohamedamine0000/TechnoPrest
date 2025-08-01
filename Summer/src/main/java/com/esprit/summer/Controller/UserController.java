package com.esprit.summer.Controller;

import com.esprit.summer.Entities.UserRegistrationDto;
import com.esprit.summer.Entities.Specialite;
import com.esprit.summer.Entities.User;
import com.esprit.summer.Repositories.SpecialiteRepo;
import com.esprit.summer.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Collections; // Import for Collections.singletonMap
import java.util.Map; // Import Map
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private SpecialiteRepo specialiteRepository;

    @PostMapping
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        // Changed return type to ResponseEntity<Map<String, String>> for consistent JSON error messages

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
        if (registrationDto.getPassword() == null || registrationDto.getPassword().length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "Password must be at least 6 characters."));
        }

        User newUser = new User();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setPassword(registrationDto.getPassword());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setCIN(registrationDto.getCin());

        if (registrationDto.getSpecialiteId() != null) {
            Optional<Specialite> specialiteOptional = specialiteRepository.findById(registrationDto.getSpecialiteId());
            if (specialiteOptional.isPresent()) {
                newUser.setSpecialite(specialiteOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("message", "Invalid Specialite ID provided."));
            }
        } else {
            // If specialite is required and not provided, you might want to return an error here
            // For now, it proceeds without a specialite if null.
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
}
