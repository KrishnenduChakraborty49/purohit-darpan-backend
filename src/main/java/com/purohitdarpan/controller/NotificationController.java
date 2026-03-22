package com.purohitdarpan.controller;

import com.purohitdarpan.entity.*;
import com.purohitdarpan.repository.*;
import com.purohitdarpan.service.FCMService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final UserRepository userRepo;
    private final NotificationLogRepository logRepo;
    private final UserNotificationPreferencesRepository prefsRepo;
    private final FCMService fcmService;

    /**
     * POST /api/notifications/register-token
     * Saves or updates the FCM device token for a user.
     */
    @PostMapping("/register-token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> registerToken(@RequestBody TokenRequest req) {
        userRepo.findById(req.getUserId()).ifPresent(user -> {
            user.setFcmToken(req.getFcmToken());
            userRepo.save(user);
        });
        return ResponseEntity.ok().build();
    }

    /**
     * PUT /api/notifications/preferences
     * Update notification preferences for a user.
     */
    @PutMapping("/preferences")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserNotificationPreferences> updatePreferences(
            @RequestBody PreferencesRequest req) {

        UserNotificationPreferences prefs = prefsRepo.findByUserId(req.getUserId())
                .orElse(UserNotificationPreferences.builder()
                        .user(userRepo.getReferenceById(req.getUserId()))
                        .build());

        if (req.getFestivalReminders() != null) prefs.setFestivalReminders(req.getFestivalReminders());
        if (req.getPanchangAlerts() != null)    prefs.setPanchangAlerts(req.getPanchangAlerts());
        if (req.getLearningReminders() != null) prefs.setLearningReminders(req.getLearningReminders());
        if (req.getPujaPractice() != null)      prefs.setPujaPractice(req.getPujaPractice());
        if (req.getReminderDaysBefore() != null) prefs.setReminderDaysBefore(req.getReminderDaysBefore());
        if (req.getEnabled() != null) {
            userRepo.findById(req.getUserId()).ifPresent(user -> {
                user.setNotificationsEnabled(req.getEnabled());
                userRepo.save(user);
            });
        }

        return ResponseEntity.ok(prefsRepo.save(prefs));
    }

    /**
     * GET /api/notifications/history/{userId}
     * Retrieve the last N notifications sent to this user.
     */
    @GetMapping("/history/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationLog>> history(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "30") int size) {
        return ResponseEntity.ok(
                logRepo.findByUserIdOrderBySentAtDesc(userId, PageRequest.of(page, size)));
    }

    /**
     * POST /api/notifications/test
     * Send a test push notification to the authenticated user (ADMIN only).
     */
    @PostMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> sendTest(@RequestBody TestRequest req) {
        Optional<User> userOpt = userRepo.findById(req.getUserId());
        if (userOpt.isEmpty() || userOpt.get().getFcmToken() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "User not found or no FCM token registered"));
        }

        String messageId = fcmService.sendNotification(
                userOpt.get().getFcmToken(),
                "🔔 Test Notification — Purohit Darpan",
                "This is a test notification from your admin panel.",
                Map.of("type", "TEST", "url", "/")
        );

        return ResponseEntity.ok(Map.of("messageId", messageId));
    }

    // ── Inner DTOs ─────────────────────────────────────────────────────────────

    @Data public static class TokenRequest {
        private Long userId;
        private String fcmToken;
    }

    @Data public static class PreferencesRequest {
        private Long userId;
        private Boolean enabled;
        private Boolean festivalReminders;
        private Boolean panchangAlerts;
        private Boolean learningReminders;
        private Boolean pujaPractice;
        private Integer reminderDaysBefore;
    }

    @Data public static class TestRequest {
        private Long userId;
    }
}
