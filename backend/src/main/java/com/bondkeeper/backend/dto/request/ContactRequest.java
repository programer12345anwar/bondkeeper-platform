package com.bondkeeper.backend.dto.request;

import com.bondkeeper.backend.entity.enums.RelationshipType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class ContactRequest {

    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 30)
    private String phoneNumber;

    @Size(max = 30)
    private String whatsappNumber;

    @Size(max = 5000)
    private String notes;

    @NotNull
    private RelationshipType relationshipType;

    @Min(0)
    @Max(100)
    private Integer relationshipScore;

    private LocalDate lastInteractionDate;

    private Boolean innerCircle;

    private Long categoryId;

    private Long priorityLevelId;
}
