package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "user_notification_preferences")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNotificationPreferences {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "festival_reminders")
    @Builder.Default
    private Boolean festivalReminders = true;

    @Column(name = "panchang_alerts")
    @Builder.Default
    private Boolean panchangAlerts = true;

    @Column(name = "learning_reminders")
    @Builder.Default
    private Boolean learningReminders = true;

    @Column(name = "puja_practice")
    @Builder.Default
    private Boolean pujaPractice = true;

    @Column(name = "reminder_days_before")
    @Builder.Default
    private Integer reminderDaysBefore = 20;

    @Column(name = "quiet_hours_start")
    @Builder.Default
    private LocalTime quietHoursStart = LocalTime.of(22, 0);

    @Column(name = "quiet_hours_end")
    @Builder.Default
    private LocalTime quietHoursEnd = LocalTime.of(7, 0);

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
