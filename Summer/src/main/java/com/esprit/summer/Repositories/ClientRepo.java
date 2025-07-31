package com.esprit.summer.Repositories;

import com.esprit.summer.Entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepo extends JpaRepository<Client, Integer> {
    Client findByNom(String nom);

    List<Client> findAllByNom(String nom);

    List<Client> findAllByPrenom(String prenom);
}

