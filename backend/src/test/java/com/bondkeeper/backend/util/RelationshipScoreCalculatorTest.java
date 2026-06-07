package com.bondkeeper.backend.util;

import com.bondkeeper.backend.entity.Contact;
import com.bondkeeper.backend.entity.Interaction;
import com.bondkeeper.backend.entity.enums.InteractionType;
import com.bondkeeper.backend.entity.enums.RelationshipType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RelationshipScoreCalculatorTest {

    @Test
    void calculate_recentInteraction_increasesScore() {
        Contact contact = new Contact();
        contact.setInnerCircle(false);
        contact.setLastInteractionDate(LocalDate.now().minusDays(3));

        Interaction interaction = new Interaction();
        interaction.setInteractionType(InteractionType.CALL);
        interaction.setInteractionDate(LocalDate.now().minusDays(3));

        int score = RelationshipScoreCalculator.calculate(contact, List.of(interaction));

        assertTrue(score >= 70);
    }

    @Test
    void calculate_noInteraction_decreasesScore() {
        Contact contact = new Contact();
        contact.setRelationshipType(RelationshipType.FRIEND);
        contact.setInnerCircle(false);
        contact.setLastInteractionDate(null);

        int score = RelationshipScoreCalculator.calculate(contact, List.of());

        assertEquals(35, score);
    }

    @Test
    void calculate_innerCircle_addsBonus() {
        Contact contact = new Contact();
        contact.setInnerCircle(true);
        contact.setLastInteractionDate(LocalDate.now().minusDays(5));

        int score = RelationshipScoreCalculator.calculate(contact, List.of());

        assertTrue(score >= 75);
    }

    @Test
    void calculate_clampsToMax100() {
        Contact contact = new Contact();
        contact.setInnerCircle(true);
        contact.setLastInteractionDate(LocalDate.now());

        Interaction i1 = interaction(InteractionType.MEETING, LocalDate.now());
        Interaction i2 = interaction(InteractionType.MEETING, LocalDate.now().minusDays(1));
        Interaction i3 = interaction(InteractionType.VIDEO_CALL, LocalDate.now().minusDays(2));

        int score = RelationshipScoreCalculator.calculate(contact, List.of(i1, i2, i3));

        assertTrue(score >= 90 && score <= 100);
    }

    private Interaction interaction(InteractionType type, LocalDate date) {
        Interaction interaction = new Interaction();
        interaction.setInteractionType(type);
        interaction.setInteractionDate(date);
        return interaction;
    }
}
