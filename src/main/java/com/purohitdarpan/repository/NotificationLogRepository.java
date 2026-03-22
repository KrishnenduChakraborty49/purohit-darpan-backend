package com.purohitdarpan.repository;

import com.purohitdarpan.entity.NotificationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    Page<NotificationLog> findByUserIdOrderBySentAtDesc(Long userId, Pageable pageable);
    boolean existsByUserIdAndFestivalId(Long userId, Long festivalId);
}
