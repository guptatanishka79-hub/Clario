package com.clario.app.dto;

import java.util.List;

public record SkillGapReportDto(
        Long careerRoleId,
        String careerRoleName,
        double readinessPercent,
        List<SkillGapItemDto> items,
        List<String> strongSkills,
        List<String> skillsToImprove,
        List<String> missingSkills
) {
}
