package com.purohitdarpan.repository;

import com.purohitdarpan.entity.UserStepNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStepNoteRepository extends JpaRepository<UserStepNote, Long> {
    List<UserStepNote> findByUserIdAndStepIdOrderByCreatedAtDesc(Long userId, Long stepId);
    List<UserStepNote> findByUserIdOrderByUpdatedAtDesc(Long userId);
    void deleteByUserIdAndId(Long userId, Long id);
}
