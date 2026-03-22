package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "pdf_annotations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PdfAnnotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    @Column(name = "annotation_text", columnDefinition = "TEXT")
    private String annotationText;

    @Column(name = "position_x")
    @Builder.Default
    private Float positionX = 0f;

    @Column(name = "position_y")
    @Builder.Default
    private Float positionY = 0f;

    @Column(length = 20)
    @Builder.Default
    private String color = "#FFD700";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
