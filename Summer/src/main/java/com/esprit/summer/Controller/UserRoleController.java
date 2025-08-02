package com.esprit.summer.Controller;

import com.esprit.summer.Entities.UserRole;
import com.esprit.summer.Repositories.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserRoleController {
    @Autowired
    private UserRoleRepo userRoleRepo;

    @PostMapping("/admin/role")
    public ResponseEntity<UserRole> addRole(@RequestBody UserRole role) {
        Optional<UserRole> existingRole = userRoleRepo.findAll().stream()
                .filter(s -> s.getName().equalsIgnoreCase(role.getName()))
                .findFirst();

        if (existingRole.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        UserRole savedRole = userRoleRepo.save(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
    }

    @GetMapping("/roles")
    public List<UserRole> getAllRoles() {
        return userRoleRepo.findAll();
    }
}
