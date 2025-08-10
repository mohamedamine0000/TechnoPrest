package com.esprit.summer.Controller;

import com.esprit.summer.Entities.ArticleMovement;
import com.esprit.summer.Repositories.ArticleMovementRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/article-movements")
public class ArticleMovementController {

    @Autowired
    private ArticleMovementRepo articleMovementRepo;

    @GetMapping
    public ResponseEntity<List<ArticleMovement>> getAllArticleMovements() {
        List<ArticleMovement> movements = articleMovementRepo.findAll();
        return ResponseEntity.ok(movements);
    }
}