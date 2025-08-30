package com.esprit.summer.Repositories;

import com.esprit.summer.Entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByName(String roleName);
}
