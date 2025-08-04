package com.esprit.summer.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime; // Using LocalDateTime for better date/time handling

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ArticleMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movementId;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
    private int quantityChange;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Reason reason; // Why the movement happened (Bought, Sold, ChangeDepo)

    private String fromLocation; // Optional: The location before the movement
    private String toLocation;   // Optional: The location after the movement

    private LocalDateTime timestamp; // When the movement occurred (e.g., sale date, purchase date)

    private String recordedBy;
}