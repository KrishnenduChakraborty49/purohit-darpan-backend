package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "step_samagri")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepSamagri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    private PujaStep step;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "samagri_id", nullable = false)
    private Samagri samagri;

    @Column(length = 100)
    private String quantity;

    @Column(length = 500)
    private String notes;
}
