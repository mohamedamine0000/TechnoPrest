package com.esprit.summer.Repositories;

import com.esprit.summer.Entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
}
