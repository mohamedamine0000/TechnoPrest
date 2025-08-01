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
@RequestMapping("/api")
public class SpecialiteController {

    @Autowired
    private SpecialiteRepo specialiteRepository;

    @PostMapping("/admin/specialites")
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

    @GetMapping("/specialites")
    public List<Specialite> getAllSpecialites() {
        return specialiteRepository.findAll();
    }
}
