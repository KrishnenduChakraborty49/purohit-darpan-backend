package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resources")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "puja_id")
    private Puja puja;

    @Column(nullable = false, length = 300)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false, length = 10)
    @Builder.Default
    private ResourceType resourceType = ResourceType.PDF;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "file_size_kb")
    private Integer fileSizeKb;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "table_of_contents", columnDefinition = "JSON")
    private String tableOfContents; // JSON: [{page, title}]

    @Column(name = "is_downloadable")
    @Builder.Default
    private Boolean isDownloadable = false;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum ResourceType { PDF, AUDIO, IMAGE, VIDEO }
}
