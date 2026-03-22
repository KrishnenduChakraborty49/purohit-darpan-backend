package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "panchang_cache")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PanchangCache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate date;

    @Column(length = 100)
    private String tithi;

    @Column(length = 100)
    private String nakshatra;

    @Column(length = 100)
    private String yoga;

    @Column(length = 100)
    private String karana;

    @Column(length = 100)
    private String vara;

    @Column(name = "rahu_start")
    private LocalTime rahuStart;

    @Column(name = "rahu_end")
    private LocalTime rahuEnd;

    @Column(name = "gulika_start")
    private LocalTime gulikaStart;

    @Column(name = "gulika_end")
    private LocalTime gulikaEnd;

    @Column(name = "yamghant_start")
    private LocalTime yamghantStart;

    @Column(name = "yamghant_end")
    private LocalTime yamghantEnd;

    @Column(name = "abhijit_start")
    private LocalTime abhijitStart;

    @Column(name = "abhijit_end")
    private LocalTime abhijitEnd;

    @Column(name = "brahma_muhurta_start")
    private LocalTime brahmaMuhurtaStart;

    @Column(name = "brahma_muhurta_end")
    private LocalTime brahmaMuhurtaEnd;

    @Column
    private LocalTime sunrise;

    @Column
    private LocalTime sunset;

    @Column(name = "is_purnima")
    @Builder.Default
    private Boolean isPurnima = false;

    @Column(name = "is_amavasya")
    @Builder.Default
    private Boolean isAmavasya = false;

    @Column(name = "is_ekadashi")
    @Builder.Default
    private Boolean isEkadashi = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
