package com.purohitdarpan.repository;

import com.purohitdarpan.entity.AiQueryLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiQueryLogRepository extends JpaRepository<AiQueryLog, Long> {
    Page<AiQueryLog> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    long countByUserIdAndQueryType(Long userId, AiQueryLog.QueryType queryType);
}
