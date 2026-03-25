package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
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

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    public Puja getPuja() {
        return puja;
    }
}
