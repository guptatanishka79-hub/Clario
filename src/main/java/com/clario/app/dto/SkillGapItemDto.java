package com.clario.app.dto;

public record SkillGapItemDto(
        Long skillId,
        String skillName,
        String requiredProficiency,
        int requiredWeight,
        String currentProficiency,
        int currentWeight,
        double contributionRatio,
        String status // STRONG, NEEDS_IMPROVEMENT, MISSING
) {
}
