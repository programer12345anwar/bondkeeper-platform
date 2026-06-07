package com.bondkeeper.backend.repository;

import com.bondkeeper.backend.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByContactIdOrderByReminderDateAsc(Long contactId);

    List<Reminder> findByReminderDateBetweenOrderByReminderDateAsc(LocalDate start, LocalDate end);

    Optional<Reminder> findByIdAndContactUserId(Long id, Long userId);
}
