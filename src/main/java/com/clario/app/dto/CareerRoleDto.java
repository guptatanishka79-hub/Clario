package com.clario.app.dto;

import java.util.List;

public record CareerRoleDto(Long id, String name, String description, List<RequiredSkillDto> requiredSkills) {
}
