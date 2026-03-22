package com.purohitdarpan.repository;

import com.purohitdarpan.entity.Mantra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MantraRepository extends JpaRepository<Mantra, Long> {
}