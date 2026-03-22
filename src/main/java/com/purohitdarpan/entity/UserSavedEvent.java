package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_saved_events",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","festival_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSavedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "festival_id", nullable = false)
    private HinduFestival festival;

    @Column(name = "reminder_set")
    @Builder.Default
    private Boolean reminderSet = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
