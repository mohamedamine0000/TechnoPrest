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
    @ManyToOne
    @JoinColumn(name = "role")
    private UserRole role;
    private String reservedByWho;
    private String reservedToWho;

    @Enumerated(EnumType.STRING)
    private Timeframe movementTimeframe;
    private Integer fastSalesThreshold;
    private Integer mediumSalesThreshold;


    private Integer datelimitNumber;
    @Enumerated(EnumType.STRING)
    private TimeUnit datelimitUnit;
    private Integer alertBeforeDatelimitNumber;
    @Enumerated(EnumType.STRING)
    private TimeUnit alertBeforeDatelimitUnit;





    public Integer getDatelimitNumber() {
        return datelimitNumber;
    }

    public void setDatelimitNumber(Integer datelimitNumber) {
        this.datelimitNumber = datelimitNumber;
    }

    public TimeUnit getDatelimitUnit() {
        return datelimitUnit;
    }

    public void setDatelimitUnit(TimeUnit datelimitUnit) {
        this.datelimitUnit = datelimitUnit;
    }

    public Integer getAlertBeforeDatelimitNumber() {
        return alertBeforeDatelimitNumber;
    }

    public void setAlertBeforeDatelimitNumber(Integer alertBeforeDatelimitNumber) {
        this.alertBeforeDatelimitNumber = alertBeforeDatelimitNumber;
    }

    public TimeUnit getAlertBeforeDatelimitUnit() {
        return alertBeforeDatelimitUnit;
    }

    public void setAlertBeforeDatelimitUnit(TimeUnit alertBeforeDatelimitUnit) {
        this.alertBeforeDatelimitUnit = alertBeforeDatelimitUnit;
    }

    public Timeframe getMovementTimeframe() {
        return movementTimeframe;
    }

    public void setMovementTimeframe(Timeframe movementTimeframe) {
        this.movementTimeframe = movementTimeframe;
    }

    public Integer getFastSalesThreshold() {
        return fastSalesThreshold;
    }

    public void setFastSalesThreshold(Integer fastSalesThreshold) {
        this.fastSalesThreshold = fastSalesThreshold;
    }

    public Integer getMediumSalesThreshold() {
        return mediumSalesThreshold;
    }

    public void setMediumSalesThreshold(Integer mediumSalesThreshold) {
        this.mediumSalesThreshold = mediumSalesThreshold;
    }

    public String getReservedByWho() {
        return reservedByWho;
    }

    public void setReservedByWho(String reservedByWho) {
        this.reservedByWho = reservedByWho;
    }

    public String getReservedToWho() {
        return reservedToWho;
    }

    public void setReservedToWho(String reservedToWho) {
        this.reservedToWho = reservedToWho;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getCodeArticle() {
        return codeArticle;
    }

    public void setCodeArticle(String codeArticle) {
        this.codeArticle = codeArticle;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public int getMinmumStock() {
        return minmumStock;
    }

    public void setMinmumStock(int minmumStock) {
        this.minmumStock = minmumStock;
    }

    public EtatArticle getEtat() {
        return etat;
    }

    public void setEtat(EtatArticle etat) {
        this.etat = etat;
    }

    public String getEtagere() {
        return etagere;
    }

    public void setEtagere(String etagere) {
        this.etagere = etagere;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }
}
