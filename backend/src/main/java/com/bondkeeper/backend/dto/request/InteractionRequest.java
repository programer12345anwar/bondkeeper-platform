package com.bondkeeper.backend.dto.request;

import com.bondkeeper.backend.entity.enums.InteractionType;
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
public class InteractionRequest {

    @NotNull
    private InteractionType interactionType;

    @NotNull
    private LocalDate interactionDate;

    @Size(max = 5000)
    private String notes;

    @NotNull
    private Long contactId;
}
