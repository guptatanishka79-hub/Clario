package com.clario.app.dto;

import java.util.List;

public record AdminStatsDto(
        long totalUsers,
        long totalAdmins,
        long totalSkills,
        long totalCareerRoles,
        List<PopularSkillDto> mostPopularSkills,
        List<UsersByRoleDto> usersByTargetRole
) {
}
