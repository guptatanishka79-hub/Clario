package com.clario.app.dto;

import java.time.LocalDate;

public record UserSkillDto(
        Long id,
        Long skillId,
        String skillName,
        Long categoryId,
        String categoryName,
        String proficiencyLevel,
        int proficiencyWeight,
        Double yearsOfExperience,
        LocalDate lastUpdated
) {
}
