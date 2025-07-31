package com.esprit.summer.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Plat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPlat;
    private  String label;
    private float prix;
    private float calories;
    @Enumerated(EnumType.STRING)
    private Categorie categorie;
    @ManyToOne
    Client client;
    @ManyToMany(cascade =
            CascadeType.ALL)
    private List<Cuisinier>
            cuisiniers;

    public List<Cuisinier> getCuisiniers() {
        return cuisiniers;
    }

    public void setCuisiniers(List<Cuisinier> cuisiniers) {
        this.cuisiniers = cuisiniers;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIdPlat() {
        return idPlat;
    }

    public void setIdPlat(int idPlat) {
        this.idPlat = idPlat;
    }

    public enum Categorie{
        Entree, Principal,Dessert
    }
}
