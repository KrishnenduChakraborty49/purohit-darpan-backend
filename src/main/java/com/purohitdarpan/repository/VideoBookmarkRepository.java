package com.purohitdarpan.repository;

import com.purohitdarpan.entity.VideoBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoBookmarkRepository extends JpaRepository<VideoBookmark, Long> {
    List<VideoBookmark> findByUserIdAndStepIdOrderByTimestampSecondsAsc(Long userId, Long stepId);
    void deleteByUserIdAndId(Long userId, Long id);
}
