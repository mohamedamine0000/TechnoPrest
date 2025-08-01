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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String password;
    private String email;
    @ManyToOne
    @JoinColumn(name = "specialite")
    private Specialite specialite;

    @ManyToOne
    @JoinColumn(name = "role")
    private UserRole role;

    @Lob
    @Column(name = "diploma_proof", columnDefinition = "LONGBLOB")
    private byte[] diplomaProof;
    @Column(unique = true, length = 8)
    private String cin;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    public byte[] getDiplomaProof() {
        return diplomaProof;
    }

    public void setDiplomaProof(byte[] diplomaProof) {
        this.diplomaProof = diplomaProof;
    }

    public String getCIN() {
        return cin;
    }

    public void setCIN(String CIN) {
        this.cin = CIN;
    }
}
