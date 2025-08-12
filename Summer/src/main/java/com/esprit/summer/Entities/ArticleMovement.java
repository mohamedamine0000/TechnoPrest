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


    private String client;
    private String clientZone;
    private String clientActivityDomain;
    private String priseEnCharge;







    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }


    public String getClientZone() {
        return clientZone;
    }

    public void setClientZone(String clientZone) {
        this.clientZone = clientZone;
    }

    public String getClientActivityDomain() {
        return clientActivityDomain;
    }

    public void setClientActivityDomain(String clientActivityDomain) {
        this.clientActivityDomain = clientActivityDomain;
    }

    public String getPriseEnCharge() {
        return priseEnCharge;
    }

    public void setPriseEnCharge(String priseEnCharge) {
        this.priseEnCharge = priseEnCharge;
    }

    public Long getMovementId() {
        return movementId;
    }

    public void setMovementId(Long movementId) {
        this.movementId = movementId;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(int quantityChange) {
        this.quantityChange = quantityChange;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }
}