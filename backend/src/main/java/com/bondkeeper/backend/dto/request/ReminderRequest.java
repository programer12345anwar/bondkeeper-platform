package com.bondkeeper.backend.dto.request;

import com.bondkeeper.backend.entity.enums.ReminderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReminderRequest {

    @NotBlank
    @Size(max = 2000)
    private String reminderMessage;

    @NotNull
    private ReminderType reminderType;

    @NotNull
    private LocalDate reminderDate;

    @NotNull
    private Long contactId;
}
