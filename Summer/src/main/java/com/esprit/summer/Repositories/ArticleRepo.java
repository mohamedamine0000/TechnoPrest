package com.esprit.summer.Repositories;

import com.esprit.summer.Entities.Article;
import com.esprit.summer.Entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepo extends JpaRepository<Article, Long> {
    Optional<Article> findByCodeArticleAndLocation(String codeArticle, String location);
    // Method for a specific user role
    @Query("SELECT a FROM Article a WHERE a.qte <= a.minmumStock AND a.role = :userRole")
    List<Article> findLowStockByRole(@Param("userRole") UserRole userRole);

    // Method for all roles (admin)
    @Query("SELECT a FROM Article a WHERE a.qte <= a.minmumStock")
    List<Article> findAllLowStock();

}
