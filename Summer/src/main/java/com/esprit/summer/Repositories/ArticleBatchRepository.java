package com.esprit.summer.Repositories;

import com.esprit.summer.Entities.Article;
import com.esprit.summer.Entities.ArticleBatch;
import com.esprit.summer.Entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ArticleBatchRepository extends JpaRepository<ArticleBatch, Long> {

    @Query("SELECT ab FROM ArticleBatch ab WHERE ab.expiryAlert IS NOT NULL AND ab.expiryAlert <= :today")
    List<ArticleBatch> findExpiredOrAlertBatches(@Param("today") LocalDate today);

    @Query("SELECT ab FROM ArticleBatch ab WHERE ab.article.role = :userRole AND :today >= ab.expiryAlert")
    List<ArticleBatch> findExpiredOrAlertBatchesByRole(@Param("userRole") UserRole userRole, @Param("today") LocalDate today);

    List<ArticleBatch> findByArticleAndExpiryDateGreaterThan(Article article, LocalDate date);


    List<ArticleBatch> findAll();

    @Query("SELECT ab FROM ArticleBatch ab WHERE ab.article.role = :userRole")
    List<ArticleBatch> findAllArticleBatchesByUserRole(@Param("userRole") UserRole userRole);


}
