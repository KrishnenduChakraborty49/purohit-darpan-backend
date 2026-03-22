package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_puja_progress",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","puja_id","step_id","format"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPujaProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puja_id", nullable = false)
    private Puja puja;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    private PujaStep step;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private LearningFormat format = LearningFormat.DOC;

    @Column
    @Builder.Default
    private Boolean completed = false;

    @UpdateTimestamp
    @Column(name = "last_accessed")
    private LocalDateTime lastAccessed;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum LearningFormat { DOC, VIDEO, PDF }
}
