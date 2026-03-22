package com.purohitdarpan.repository;

import com.purohitdarpan.entity.HinduFestival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HinduFestivalRepository extends JpaRepository<HinduFestival, Long> {

    @Query("SELECT f FROM HinduFestival f WHERE f.isActive = true " +
           "AND f.eventDate BETWEEN :from AND :to ORDER BY f.eventDate ASC")
    List<HinduFestival> findUpcoming(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("SELECT f FROM HinduFestival f WHERE f.isActive = true " +
           "AND FUNCTION('YEAR', f.eventDate) = :year ORDER BY f.eventDate ASC")
    List<HinduFestival> findByYear(@Param("year") int year);

    /**
     * Finds festivals whose custom notification window starts today.
     * The caller passes today; we compute the target date in Java.
     * Query: festivals where eventDate == today + notificationDaysBefore
     * This is done by passing the range from the service layer.
     */
    @Query("SELECT f FROM HinduFestival f WHERE f.isActive = true " +
           "AND f.eventDate BETWEEN :rangeStart AND :rangeEnd")
    List<HinduFestival> findFestivalsNeedingNotification(
            @Param("rangeStart") LocalDate rangeStart,
            @Param("rangeEnd") LocalDate rangeEnd);

    /**
     * Finds festivals that fall exactly on the given target date.
     * Caller computes: targetDate = today + days.
     */
    @Query("SELECT f FROM HinduFestival f WHERE f.isActive = true " +
           "AND f.eventDate = :targetDate")
    List<HinduFestival> findFestivalsInDays(@Param("targetDate") LocalDate targetDate);
}
