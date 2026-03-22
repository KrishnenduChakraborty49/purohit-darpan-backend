package com.purohitdarpan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private HinduFestival festival;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 30)
    private NotificationType notificationType;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "action_url", length = 500)
    private String actionUrl;

    @CreationTimestamp
    @Column(name = "sent_at", updatable = false)
    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", length = 15)
    @Builder.Default
    private DeliveryStatus deliveryStatus = DeliveryStatus.SENT;

    @Column(name = "fcm_message_id", length = 500)
    private String fcmMessageId;

    public enum NotificationType {
        FESTIVAL_REMINDER, PUJA_PRACTICE, PANCHANG_ALERT, LEARNING_REMINDER
    }

    public enum DeliveryStatus { SENT, FAILED, DELIVERED }
}
