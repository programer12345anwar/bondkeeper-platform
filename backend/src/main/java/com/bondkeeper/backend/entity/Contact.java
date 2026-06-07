package com.bondkeeper.backend.entity;

import com.bondkeeper.backend.entity.enums.RelationshipType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Contact extends AuditableEntity {

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "whatsapp_number", length = 30)
    private String whatsappNumber;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_type", nullable = false, length = 50)
    private RelationshipType relationshipType;

    @Column(name = "relationship_score", nullable = false)
    @Builder.Default
    private Integer relationshipScore = 50;

    @Column(name = "last_interaction_date")
    private LocalDate lastInteractionDate;

    @Column(name = "inner_circle", nullable = false)
    @Builder.Default
    private Boolean innerCircle = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_level_id")
    private PriorityLevel priorityLevel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Interaction> interactions = new ArrayList<>();

    @OneToMany(mappedBy = "contact", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Reminder> reminders = new ArrayList<>();
}
