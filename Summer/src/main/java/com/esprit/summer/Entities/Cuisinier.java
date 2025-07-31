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
public class Cuisinier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCuisinier;
    private String nom;
    private String prenom;
    @ManyToMany(mappedBy="cuisiniers",
    cascade = CascadeType.ALL)
    private List<Plat> plats;

    public List<Plat> getPlats() {
        return plats;
    }

    public void setPlats(List<Plat> plats) {
        this.plats = plats;
    }

    public int getIdCuisinier() {
        return idCuisinier;
    }

    public void setIdCuisinier(int idCuisinier) {
        this.idCuisinier = idCuisinier;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
}
