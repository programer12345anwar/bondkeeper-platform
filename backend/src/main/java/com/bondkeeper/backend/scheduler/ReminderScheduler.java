package com.bondkeeper.backend.scheduler;

import com.bondkeeper.backend.service.ReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Scheduled jobs for reminder processing.
 * Notification delivery will be wired in Phase 2.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final ReminderService reminderService;

    @Scheduled(cron = "0 0 8 * * *")
    public void processDueReminders() {
        LocalDate today = LocalDate.now();
        var dueReminders = reminderService.getDueBetween(today, today);
        log.info("Found {} reminders due today", dueReminders.size());
    }
}
