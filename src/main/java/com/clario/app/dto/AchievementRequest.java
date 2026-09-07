package com.clario.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AchievementRequest {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private LocalDate achievementDate;
    private String organization;
}
