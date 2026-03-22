package com.purohitdarpan.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FCMService {

    /**
     * Send a push notification to a single device token.
     */
    public String sendNotification(String deviceToken, String title, String body,
                                    Map<String, String> data) {
        if (FirebaseApp.getApps().isEmpty()) {
            log.warn("Firebase not initialized — skipping notification to {}", deviceToken);
            return "FIREBASE_NOT_INITIALIZED";
        }

        try {
            Message.Builder builder = Message.builder()
                    .setToken(deviceToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build());

            if (data != null && !data.isEmpty()) {
                builder.putAllData(data);
            }

            // Android config for high priority
            builder.setAndroidConfig(AndroidConfig.builder()
                    .setPriority(AndroidConfig.Priority.HIGH)
                    .setNotification(AndroidNotification.builder()
                            .setChannelId("purohit_darpan_main")
                            .setIcon("ic_notification")
                            .setColor("#FF6B00")
                            .build())
                    .build());

            String messageId = FirebaseMessaging.getInstance().send(builder.build());
            log.info("FCM sent: {}", messageId);
            return messageId;

        } catch (FirebaseMessagingException e) {
            log.error("FCM send failed for token {}: {}", deviceToken, e.getMessage());
            return "FAILED";
        }
    }

    /**
     * Send a notification to multiple device tokens.
     * Returns list of (token, messageId/error) pairs.
     */
    public BatchResponse sendToMultipleDevices(List<String> tokens, String title,
                                                String body, Map<String, String> data) {
        if (FirebaseApp.getApps().isEmpty() || tokens.isEmpty()) {
            log.warn("Firebase not initialized or no tokens — skipping multicast");
            return null;
        }

        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            MulticastMessage.Builder builder = MulticastMessage.builder()
                    .addAllTokens(tokens)
                    .setNotification(notification)
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .build());

            if (data != null && !data.isEmpty()) {
                builder.putAllData(data);
            }

            BatchResponse response = FirebaseMessaging.getInstance()
                    .sendEachForMulticast(builder.build());

            log.info("FCM multicast: {} success, {} failure out of {} tokens",
                    response.getSuccessCount(), response.getFailureCount(), tokens.size());

            return response;

        } catch (FirebaseMessagingException e) {
            log.error("FCM multicast failed: {}", e.getMessage());
            return null;
        }
    }

    /** Remove invalid tokens from the system after a failed delivery */
    public boolean isTokenValid(String token) {
        if (FirebaseApp.getApps().isEmpty()) return false;
        try {
            // Send a dry-run message to validate the token
            Message msg = Message.builder()
                    .setToken(token)
                    .build();
            FirebaseMessaging.getInstance().send(msg, true); // dry run
            return true;
        } catch (FirebaseMessagingException e) {
            return false;
        }
    }
}
