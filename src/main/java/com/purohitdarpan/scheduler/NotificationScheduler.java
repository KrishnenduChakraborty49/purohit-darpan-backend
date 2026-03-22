package com.purohitdarpan.scheduler;

import com.purohitdarpan.entity.HinduFestival;
import com.purohitdarpan.entity.NotificationLog;
import com.purohitdarpan.entity.User;
import com.purohitdarpan.entity.UserNotificationPreferences;
import com.purohitdarpan.repository.HinduFestivalRepository;
import com.purohitdarpan.repository.NotificationLogRepository;
import com.purohitdarpan.repository.UserNotificationPreferencesRepository;
import com.purohitdarpan.repository.UserRepository;
import com.purohitdarpan.service.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationScheduler {

    private final HinduFestivalRepository festivalRepo;
    private final UserRepository userRepo;
    private final UserNotificationPreferencesRepository prefsRepo;
    private final NotificationLogRepository notifLogRepo;
    private final FCMService fcmService;

    /**
     * Runs every day at 8:00 AM IST.
     * Checks upcoming festivals and sends timely reminders.
     */
    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Kolkata")
    public void sendFestivalReminders() {
        LocalDate today = LocalDate.now();
        log.info("Running festival notification scheduler for date: {}", today);

        // Check for festivals at multiple day windows
        int[] daysBeforeWindows = {20, 7, 1, 0};

        for (int window : daysBeforeWindows) {
            List<HinduFestival> festivals = festivalRepo.findFestivalsInDays(
                    today.plusDays(window));
            for (HinduFestival festival : festivals) {
                processWindowNotification(festival, window, today);
            }
        }

        // Also check festivals with custom notification_days_before (range: today+21 to today+365)
        List<HinduFestival> customFestivals = festivalRepo.findFestivalsNeedingNotification(
                today.plusDays(21), today.plusDays(365));
        for (HinduFestival festival : customFestivals) {
            long daysUntil = today.until(festival.getEventDate(),
                    java.time.temporal.ChronoUnit.DAYS);
            if (daysUntil > 20) { // Only if not already handled by standard windows
                processCustomNotification(festival, (int) daysUntil, today);
            }
        }

        log.info("Festival notification scheduler completed for {}", today);
    }

    /**
     * Runs every Monday at 9:00 AM — sends learning reminders to inactive users.
     */
    @Scheduled(cron = "0 0 9 * * MON", zone = "Asia/Kolkata")
    public void sendLearningReminders() {
        log.info("Running learning reminder scheduler");

        List<User> allUsers = userRepo.findByRoleInAndNotificationsEnabledTrue(
                List.of(User.Role.STUDENT, User.Role.MENTOR));

        for (User user : allUsers) {
            if (user.getFcmToken() == null) continue;

            Optional<UserNotificationPreferences> prefsOpt = prefsRepo.findByUserId(user.getId());
            if (prefsOpt.isEmpty() || !prefsOpt.get().getLearningReminders()) continue;

            if (isInQuietHours(user.getId())) continue;

            String title = "📚 Keep learning, " + user.getFullName().split(" ")[0] + "!";
            String body  = "Continue your Puja studies. Consistency is the key to mastery.";

            sendAndLog(user, null, NotificationLog.NotificationType.LEARNING_REMINDER,
                    title, body, "/pujas", "LEARNING_REMINDER");
        }
    }

    // ── Private helpers ────────────────────────────────────────────────────────

    private void processWindowNotification(HinduFestival festival, int daysLeft, LocalDate today) {
        String title, body, url;

        String pujaId = festival.getPuja() != null ? festival.getPuja().getId().toString() : "";
        String pujaPart = pujaId.isBlank() ? "/pujas" : "/puja/" + pujaId;

        if (daysLeft == 0) {
            title = "🙏 Today is " + festival.getName();
            body  = "Begin the guided puja. Open the step-by-step ritual module and perform with confidence.";
            url   = pujaPart + "/player";
        } else if (daysLeft == 1) {
            title = "🌸 " + festival.getName() + " is tomorrow!";
            body  = "Check today's Muhurat and prepare your Samagri checklist now.";
            url   = pujaPart;
        } else if (daysLeft == 7) {
            title = "⏰ " + festival.getName() + " — 1 week away";
            body  = "Review the complete puja steps and check the Panchang for auspicious timings.";
            url   = "/panchang";
        } else {
            title = "🪔 " + festival.getName() + " in " + daysLeft + " days";
            body  = "Prepare early! Open the learning module and start practicing today.";
            url   = pujaPart;
        }

        sendToAllEligibleUsers(festival, title, body, url,
                NotificationLog.NotificationType.FESTIVAL_REMINDER);
    }

    private void processCustomNotification(HinduFestival festival, int daysLeft, LocalDate today) {
        String title = "🪔 " + festival.getName() + " in " + daysLeft + " days";
        String body  = "Start your preparation for " + festival.getName();
        String url   = festival.getPuja() != null
                ? "/puja/" + festival.getPuja().getId()
                : "/festivals/" + festival.getId();

        sendToAllEligibleUsers(festival, title, body, url,
                NotificationLog.NotificationType.FESTIVAL_REMINDER);
    }

    private void sendToAllEligibleUsers(HinduFestival festival, String title,
                                         String body, String url,
                                         NotificationLog.NotificationType type) {
        List<User> users = userRepo.findByRoleInAndNotificationsEnabledTrue(
                List.of(User.Role.STUDENT, User.Role.MENTOR));

        for (User user : users) {
            if (user.getFcmToken() == null) continue;

            Optional<UserNotificationPreferences> prefsOpt = prefsRepo.findByUserId(user.getId());
            if (prefsOpt.isPresent() && !prefsOpt.get().getFestivalReminders()) continue;
            if (isInQuietHours(user.getId())) continue;

            sendAndLog(user, festival, type, title, body, url, festival.getName());
        }
    }

    private void sendAndLog(User user, HinduFestival festival,
                             NotificationLog.NotificationType type,
                             String title, String body, String url, String tag) {
        try {
            Map<String, String> data = Map.of(
                    "type", type.name(),
                    "url", url,
                    "tag", tag
            );

            String messageId = fcmService.sendNotification(user.getFcmToken(), title, body, data);
            NotificationLog.DeliveryStatus status = "FAILED".equals(messageId)
                    ? NotificationLog.DeliveryStatus.FAILED
                    : NotificationLog.DeliveryStatus.SENT;

            NotificationLog notifLog = NotificationLog.builder()
                    .user(user)
                    .festival(festival)
                    .notificationType(type)
                    .title(title)
                    .body(body)
                    .actionUrl(url)
                    .deliveryStatus(status)
                    .fcmMessageId(messageId)
                    .build();

            notifLogRepo.save(notifLog);

        } catch (Exception e) {
            log.error("Failed to send/log notification for user {}: {}", user.getId(), e.getMessage());
        }
    }

    private boolean isInQuietHours(Long userId) {
        Optional<UserNotificationPreferences> prefs = prefsRepo.findByUserId(userId);
        if (prefs.isEmpty()) return false;

        LocalTime now   = LocalTime.now();
        LocalTime start = prefs.get().getQuietHoursStart();
        LocalTime end   = prefs.get().getQuietHoursEnd();

        // Handle day-wrap (e.g., 22:00 -> 07:00)
        if (start.isAfter(end)) {
            return now.isAfter(start) || now.isBefore(end);
        } else {
            return now.isAfter(start) && now.isBefore(end);
        }
    }
}
