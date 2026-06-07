package com.bondkeeper.backend.dto.response;

import com.bondkeeper.backend.entity.enums.RelationshipType;
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
public class ContactResponse {

    private Long id;
    private String name;
    private String phoneNumber;
    private String whatsappNumber;
    private String notes;
    private RelationshipType relationshipType;
    private Integer relationshipScore;
    private LocalDate lastInteractionDate;
    private Boolean innerCircle;
    private Long categoryId;
    private Long priorityLevelId;
    private Long userId;
    private Instant createdAt;
    private Instant updatedAt;
}
