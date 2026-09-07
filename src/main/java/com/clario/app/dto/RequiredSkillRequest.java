package com.clario.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequiredSkillRequest {
    @NotNull(message = "skillId is required")
    private Long skillId;
    @NotBlank(message = "requiredProficiency is required")
    private String requiredProficiency;
}
