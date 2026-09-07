package com.clario.app.dto;

import java.util.List;

public record SkillProgressDto(Long userSkillId, String skillName, List<SkillProgressPointDto> history) {
}
