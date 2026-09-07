package com.clario.app.dto;

import java.time.LocalDate;

public record SkillProgressPointDto(String proficiencyLevel, int proficiencyWeight, LocalDate recordedDate) {
}
