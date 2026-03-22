package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "step_mantras")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepMantra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "step_id", nullable = false)
    private PujaStep step;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mantra_id", nullable = false)
    private Mantra mantra;

    @Column(name = "sequence_order")
    @Builder.Default
    private Integer sequenceOrder = 1;
}
