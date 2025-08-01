package com.esprit.summer.Repositories;
import com.esprit.summer.Entities.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface SpecialiteRepo extends JpaRepository<Specialite, Long> {
}
