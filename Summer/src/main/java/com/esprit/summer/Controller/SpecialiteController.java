package com.esprit.summer.Controller;

import com.esprit.summer.Entities.Specialite;
import com.esprit.summer.Repositories.SpecialiteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api") // Base path for general API endpoints
public class SpecialiteController { // Renamed from previous SpecialiteController to avoid conflict if both exist

    @Autowired
    private SpecialiteRepo specialiteRepository;

    /**
     * Endpoint to add a new Specialite.
     * This would typically be an ADMIN-only endpoint.
     * @param specialite The Specialite object to add (expects JSON like: {"name": "Cardiology"})
     * @return The saved Specialite object with its generated ID.
     */
    @PostMapping("/admin/specialites") // Specific path for admin
    public ResponseEntity<Specialite> addSpecialite(@RequestBody Specialite specialite) {
        Optional<Specialite> existingSpecialite = specialiteRepository.findAll().stream()
                .filter(s -> s.getName().equalsIgnoreCase(specialite.getName()))
                .findFirst();

        if (existingSpecialite.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        Specialite savedSpecialite = specialiteRepository.save(specialite);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSpecialite);
    }

    /**
     * Endpoint to get all existing Specialites.
     * This endpoint is public for the frontend to populate dropdowns.
     * @return A list of all Specialite objects.
     */
    @GetMapping("/specialites") // Public endpoint for fetching specialities
    public List<Specialite> getAllSpecialites() {
        return specialiteRepository.findAll();
    }
}
