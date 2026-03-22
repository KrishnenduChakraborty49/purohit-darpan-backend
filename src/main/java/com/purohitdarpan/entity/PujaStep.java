package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "puja_steps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PujaStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puja_id", nullable = false)
    private Puja puja;

    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(name = "title_devanagari", length = 300)
    private String titleDevanagari;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(name = "video_url", length = 500)
    private String videoUrl;

    @Column(name = "video_transcript", columnDefinition = "LONGTEXT")
    private String videoTranscript;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdf_resource_id")
    private Resource pdfResource;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequenceOrder ASC")
    private List<StepMantra> stepMantras;

    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StepSamagri> stepSamagri;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
