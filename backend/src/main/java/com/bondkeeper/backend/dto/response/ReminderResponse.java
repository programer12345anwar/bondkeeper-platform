package com.bondkeeper.backend.dto.response;

import com.bondkeeper.backend.entity.enums.ReminderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderResponse {

    private Long id;
    private String reminderMessage;
    private ReminderType reminderType;
    private LocalDate reminderDate;
    private Long contactId;
    private Instant createdAt;
    private Instant updatedAt;
}
