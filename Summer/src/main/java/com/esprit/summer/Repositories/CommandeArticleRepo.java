package com.esprit.summer.Repositories;

import com.esprit.summer.Entities.CommandeArticleMV;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommandeArticleRepo extends JpaRepository<CommandeArticleMV, Long> {
    List<CommandeArticleMV> findByArticle_ArticleId(Long articleId);

    @Query("SELECT c FROM CommandeArticleMV c JOIN c.article a WHERE a.role.id = :roleId ORDER BY c.timestamp DESC")
    List<CommandeArticleMV> findByArticle_Role_IdOrderByTimestampDesc(@Param("roleId") Long roleId);

    List<CommandeArticleMV> findAllByOrderByTimestampDesc();

}
