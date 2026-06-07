package com.bondkeeper.backend.util;

import com.bondkeeper.backend.entity.Contact;
import com.bondkeeper.backend.entity.Interaction;
import com.bondkeeper.backend.entity.enums.InteractionType;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@UtilityClass
public class RelationshipScoreCalculator {

    private static final int BASE_SCORE = 50;
    private static final int MIN_SCORE = 0;
    private static final int MAX_SCORE = 100;

    public static int calculate(Contact contact, List<Interaction> recentInteractions) {
        int score = BASE_SCORE;
        LocalDate today = LocalDate.now();

        if (contact.getLastInteractionDate() != null) {
            long daysSince = ChronoUnit.DAYS.between(contact.getLastInteractionDate(), today);
            if (daysSince <= 7) {
                score += 20;
            } else if (daysSince <= 14) {
                score += 10;
            } else if (daysSince <= 30) {
                score += 0;
            } else if (daysSince <= 60) {
                score -= 10;
            } else {
                score -= 25;
            }
        } else {
            score -= 15;
        }

        long recentCount = recentInteractions.stream()
                .filter(i -> !i.getInteractionDate().isBefore(today.minusDays(90)))
                .count();
        score += Math.min(15, (int) recentCount * 5);

        score += recentInteractions.stream()
                .filter(i -> !i.getInteractionDate().isBefore(today.minusDays(30)))
                .mapToInt(RelationshipScoreCalculator::interactionTypeBonus)
                .sum();

        if (Boolean.TRUE.equals(contact.getInnerCircle())) {
            score += 5;
        }

        return clamp(score, MIN_SCORE, MAX_SCORE);
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static int interactionTypeBonus(Interaction interaction) {
        if (interaction.getInteractionType() == InteractionType.MEETING) {
            return 3;
        }
        if (interaction.getInteractionType() == InteractionType.VIDEO_CALL) {
            return 2;
        }
        if (interaction.getInteractionType() == InteractionType.CALL) {
            return 2;
        }
        return 1;
    }
}
