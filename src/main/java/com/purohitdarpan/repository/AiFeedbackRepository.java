package com.purohitdarpan.repository;

import com.purohitdarpan.entity.AiFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiFeedbackRepository extends JpaRepository<AiFeedback, Long> {
    List<AiFeedback> findByQueryLogId(Long queryLogId);
    List<AiFeedback> findByUserId(Long userId);
}
