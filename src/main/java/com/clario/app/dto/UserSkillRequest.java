package com.clario.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserSkillRequest {
    // Either an existing skillId, or a brand-new skillName + categoryId to create one on the fly.
    private Long skillId;

    private String skillName;

    private Long categoryId;

    @NotBlank(message = "Proficiency level is required")
    private String proficiencyLevel;

    private Double yearsOfExperience;
}
