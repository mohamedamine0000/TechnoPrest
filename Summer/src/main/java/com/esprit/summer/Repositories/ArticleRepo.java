package com.esprit.summer.Repositories;

import com.esprit.summer.Entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepo extends JpaRepository<Article, Long> {
    Optional<Article> findByCodeArticleAndLocation(String codeArticle, String location);
}
