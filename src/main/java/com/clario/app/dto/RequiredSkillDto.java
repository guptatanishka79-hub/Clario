package com.clario.app.dto;

public record RequiredSkillDto(Long skillId, String skillName, String requiredProficiency, int requiredWeight) {
}
