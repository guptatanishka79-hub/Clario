package com.clario.app.dto;

import java.time.LocalDate;

public record AchievementDto(Long id, String title, String description, LocalDate achievementDate, String organization) {
}
