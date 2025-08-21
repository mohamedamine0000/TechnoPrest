package com.esprit.summer.Repositories;

import com.esprit.summer.Entities.CommandeArticleMV;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeArticleRepo extends JpaRepository<CommandeArticleMV, Long> {
}
