package com.purohitdarpan.repository;

import com.purohitdarpan.entity.UserHighlight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHighlightRepository extends JpaRepository<UserHighlight, Long> {
    List<UserHighlight> findByUserIdAndStepId(Long userId, Long stepId);
    void deleteByUserIdAndId(Long userId, Long id);
}
