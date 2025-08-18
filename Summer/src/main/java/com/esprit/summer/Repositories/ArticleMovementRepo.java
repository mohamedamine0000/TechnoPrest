package com.esprit.summer.Repositories;
import java.time.LocalDateTime;
import java.util.List;

import com.esprit.summer.Entities.Article;
import com.esprit.summer.Entities.ArticleMovement;
import com.esprit.summer.Entities.Reason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface ArticleMovementRepo extends JpaRepository<ArticleMovement, Long> {

    List<ArticleMovement> findByArticleAndTimestampBetweenAndReasonIsNot(Article article, LocalDateTime startDate, LocalDateTime endDate, Reason reasonToExclude);
    List<ArticleMovement> findByArticleAndTimestampBetween(Article article, LocalDateTime startDate, LocalDateTime endDate);
    long countByArticleAndReasonAndTimestampBetween(Article article, Reason reason, LocalDateTime startDate, LocalDateTime endDate);
    List<ArticleMovement> findByArticle(Article article);
    @Modifying
    @Query("DELETE FROM ArticleMovement m WHERE m.article.articleId = :articleId")
    void deleteAllByArticleId(@Param("articleId") Long articleId);
}