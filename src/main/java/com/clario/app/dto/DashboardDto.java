package com.clario.app.dto;

import java.util.List;

public record DashboardDto(
        int profileCompletionPercent,
        Double careerReadinessPercent,
        String targetRoleName,
        int totalSkills,
        int totalCertifications,
        List<String> strongSkills,
        List<String> skillsToImprove,
        List<String> missingSkills,
        List<ActivityLogDto> recentActivity,
        List<SkillProgressDto> progressChart
) {
}
