package com.purohitdarpan.repository;

import com.purohitdarpan.entity.PanchangCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PanchangCacheRepository extends JpaRepository<PanchangCache, Long> {
    Optional<PanchangCache> findByDate(LocalDate date);
    boolean existsByDate(LocalDate date);

    @Query("SELECT p FROM PanchangCache p WHERE p.date >= :start AND p.date < :end ORDER BY p.date ASC")
    List<PanchangCache> findByDateBetween(LocalDate start, LocalDate end);
}
