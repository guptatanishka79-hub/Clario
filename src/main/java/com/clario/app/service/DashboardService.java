package com.clario.app.service;

import com.clario.app.dto.*;
import com.clario.app.entity.User;
import com.clario.app.entity.UserProfile;
import com.clario.app.repository.CertificationRepository;
import com.clario.app.repository.UserProfileRepository;
import com.clario.app.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserProfileRepository userProfileRepository;
    private final UserSkillRepository userSkillRepository;
    private final CertificationRepository certificationRepository;
    private final SkillGapService skillGapService;
    private final ActivityService activityService;
    private final ProgressService progressService;

    public DashboardDto buildDashboard(User user) {
        UserProfile profile = userProfileRepository.findByUser_Id(user.getId())
                .orElseGet(() -> UserProfile.builder().user(user).build());
        int profileCompletion = ProfileService.calculateCompletion(user, profile);

        int totalSkills = (int) userSkillRepository.countByUser_Id(user.getId());
        int totalCertifications = (int) certificationRepository.countByUser_Id(user.getId());

        Optional<SkillGapReportDto> gapReport = skillGapService.getReportForUser(user.getId());

        Double readiness = gapReport.map(SkillGapReportDto::readinessPercent).orElse(null);
        String targetRoleName = gapReport.map(SkillGapReportDto::careerRoleName).orElse(null);
        List<String> strong = gapReport.map(SkillGapReportDto::strongSkills).orElse(List.of());
        List<String> improve = gapReport.map(SkillGapReportDto::skillsToImprove).orElse(List.of());
        List<String> missing = gapReport.map(SkillGapReportDto::missingSkills).orElse(List.of());

        List<ActivityLogDto> recentActivity = activityService.recentForUser(user.getId(), 8);
        List<SkillProgressDto> progressChart = progressService.getProgressForUser(user.getId());

        return new DashboardDto(
                profileCompletion,
                readiness,
                targetRoleName,
                totalSkills,
                totalCertifications,
                strong,
                improve,
                missing,
                recentActivity,
                progressChart
        );
    }
}
