package com.bondkeeper.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(
        name = "priority_levels",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "level_name"})
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PriorityLevel extends AuditableEntity {

    @Column(name = "level_name", nullable = false, length = 100)
    private String levelName;

    @Column(name = "reminder_frequency_days", nullable = false)
    private Integer reminderFrequencyDays;

    @Column(name = "color_code", nullable = false, length = 7)
    private String colorCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
