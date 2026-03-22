package com.purohitdarpan.repository;

import com.purohitdarpan.entity.UserPujaProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPujaProgressRepository extends JpaRepository<UserPujaProgress, Long> {

    List<UserPujaProgress> findByUserIdAndPujaId(Long userId, Long pujaId);

    Optional<UserPujaProgress> findByUserIdAndPujaIdAndStepIdAndFormat(
            Long userId, Long pujaId, Long stepId, UserPujaProgress.LearningFormat format);

    @Query("SELECT COUNT(p) FROM UserPujaProgress p WHERE p.user.id = :userId " +
           "AND p.puja.id = :pujaId AND p.completed = true AND p.format = :format")
    long countCompletedSteps(Long userId, Long pujaId, UserPujaProgress.LearningFormat format);

    @Query("SELECT p FROM UserPujaProgress p WHERE p.user.id = :userId " +
           "ORDER BY p.lastAccessed DESC")
    List<UserPujaProgress> findRecentByUserId(Long userId, org.springframework.data.domain.Pageable pageable);
}
