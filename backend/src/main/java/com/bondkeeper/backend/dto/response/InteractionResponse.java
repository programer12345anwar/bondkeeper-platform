package com.bondkeeper.backend.dto.response;

import com.bondkeeper.backend.entity.enums.InteractionType;
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
public class InteractionResponse {

    private Long id;
    private InteractionType interactionType;
    private LocalDate interactionDate;
    private String notes;
    private Long contactId;
    private Integer updatedRelationshipScore;
    private Instant createdAt;
    private Instant updatedAt;
}
