package com.esprit.summer.Repositories;

import com.esprit.summer.Entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepo extends JpaRepository<Article, Long> {
}
