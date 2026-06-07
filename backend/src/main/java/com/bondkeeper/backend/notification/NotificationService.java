package com.bondkeeper.backend.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Notification delivery placeholder for Phase 2 (email, push, SMS).
 */
@Slf4j
@Service
public class NotificationService {

    public void sendReminderNotification(String recipient, String message) {
        log.info("Notification placeholder - recipient: {}, message: {}", recipient, message);
    }
}
