package com.bondkeeper.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriorityLevelResponse {

    private Long id;
    private String levelName;
    private Integer reminderFrequencyDays;
    private String colorCode;
    private Long userId;
    private Instant createdAt;
    private Instant updatedAt;
}
