package com.esprit.summer.Repositories;

import com.esprit.summer.Entities.Status;
import com.esprit.summer.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByCin(String cin);
    List<User> findByStatus(Status status);
    Optional<User> findByUserId(Long userId);


}
