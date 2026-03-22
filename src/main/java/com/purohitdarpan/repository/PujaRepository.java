package com.purohitdarpan.repository;

import com.purohitdarpan.entity.Puja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PujaRepository extends JpaRepository<Puja, Long> {
    List<Puja> findByIsActiveTrueOrderByNameAsc();
    Page<Puja> findByIsActiveTrue(Pageable pageable);
    List<Puja> findByCategoryAndIsActiveTrue(String category);
    List<Puja> findByDifficultyAndIsActiveTrue(Puja.Difficulty difficulty);

    @Query("SELECT p FROM Puja p WHERE p.isActive = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(p.category) LIKE LOWER(CONCAT('%',:q,'%')))")
    List<Puja> searchByKeyword(String q);
}
