package com.esprit.summer.Repositories;

import com.esprit.summer.Entities.CommandeArticleMV;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeArticleRepo extends JpaRepository<CommandeArticleMV, Long> {
    List<CommandeArticleMV> findByArticle_ArticleId(Long articleId);
}
