package com.clario.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CareerGoalRequest {
    @NotNull(message = "careerRoleId is required")
    private Long careerRoleId;
}
