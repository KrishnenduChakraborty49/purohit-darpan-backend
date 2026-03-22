package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_query_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiQueryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "query_type", nullable = false, length = 20)
    private QueryType queryType;

    @Column(name = "query_text", nullable = false, columnDefinition = "TEXT")
    private String queryText;

    @Column(name = "context_puja_id")
    private Long contextPujaId;

    @Column(name = "context_step_id")
    private Long contextStepId;

    @Column(name = "context_shlok", columnDefinition = "TEXT")
    private String contextShlok;

    @Column(name = "response_text", columnDefinition = "LONGTEXT")
    private String responseText;

    @Column(name = "tokens_used")
    private Integer tokensUsed;

    @Column(name = "response_time_ms")
    private Integer responseTimeMs;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum QueryType { WORD_QUERY, SHLOK_QUERY, GENERAL_QUESTION }
}
