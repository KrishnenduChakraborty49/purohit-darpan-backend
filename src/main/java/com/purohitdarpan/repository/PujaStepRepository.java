package com.purohitdarpan.repository;

import com.purohitdarpan.entity.PujaStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PujaStepRepository extends JpaRepository<PujaStep, Long> {
    List<PujaStep> findByPujaIdOrderByStepOrderAsc(Long pujaId);
    Optional<PujaStep> findByPujaIdAndStepOrder(Long pujaId, Integer stepOrder);
    int countByPujaId(Long pujaId);
}
