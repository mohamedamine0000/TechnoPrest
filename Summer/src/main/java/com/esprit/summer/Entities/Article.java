package com.esprit.summer.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;
    private String codeArticle;
    private String designation;
    private String um;
    private int qte;
    private Date date;
    private String location;
    private String etagere;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EtatArticle etat;
    private int minmumStock;
    private String addedBy;
    private String unite;

}
