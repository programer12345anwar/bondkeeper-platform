package com.bondkeeper.backend.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriorityLevelRequest {

    @NotBlank
    @Size(max = 100)
    private String levelName;

    @NotNull
    @Min(1)
    @Max(365)
    private Integer reminderFrequencyDays;

    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color code must be a valid hex color (e.g. #FF5733)")
    private String colorCode;
}
