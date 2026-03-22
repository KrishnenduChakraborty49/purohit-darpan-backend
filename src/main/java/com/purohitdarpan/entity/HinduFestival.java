package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "hindu_festivals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HinduFestival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String name;

    @Column(name = "name_devanagari", length = 300)
    private String nameDevanagari;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puja_id")
    private Puja puja;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "days_duration")
    @Builder.Default
    private Integer daysDuration = 1;

    @Column(name = "notification_days_before")
    @Builder.Default
    private Integer notificationDaysBefore = 20;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
