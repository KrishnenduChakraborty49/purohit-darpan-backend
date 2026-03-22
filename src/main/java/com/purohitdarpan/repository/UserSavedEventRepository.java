package com.purohitdarpan.repository;

import com.purohitdarpan.entity.UserSavedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSavedEventRepository extends JpaRepository<UserSavedEvent, Long> {
    List<UserSavedEvent> findByUserId(Long userId);
    boolean existsByUserIdAndFestivalId(Long userId, Long festivalId);
}
