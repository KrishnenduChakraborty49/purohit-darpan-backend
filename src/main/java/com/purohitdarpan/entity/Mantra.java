package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Entity
@Table(name = "mantras")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mantra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shlok_text", nullable = false, columnDefinition = "TEXT")
    private String shlokText;

    @Column(columnDefinition = "TEXT")
    private String transliteration;

    @Column(name = "word_meanings", columnDefinition = "JSON")
    private String wordMeanings; // JSON stored as String, parsed in service layer

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    @Column(name = "source_text", length = 300)
    private String sourceText;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
