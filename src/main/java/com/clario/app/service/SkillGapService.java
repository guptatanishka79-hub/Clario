package com.clario.app.service;

import com.clario.app.dto.SkillGapItemDto;
import com.clario.app.dto.SkillGapReportDto;
import com.clario.app.entity.CareerRole;
import com.clario.app.entity.CareerRoleSkill;
import com.clario.app.entity.ProficiencyLevel;
import com.clario.app.entity.UserSkill;
import com.clario.app.exception.ResourceNotFoundException;
import com.clario.app.repository.CareerRoleRepository;
import com.clario.app.repository.UserCareerGoalRepository;
import com.clario.app.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implements the Clario skill-gap calculation described in the project spec.
 *
 * The algorithm is intentionally simple and fully transparent (no ML):
 *
 *   1. Each proficiency level has a numeric weight: BEGINNER=1, INTERMEDIATE=2, ADVANCED=3, EXPERT=4.
 *   2. For every skill required by the target career role, we compare the user's
 *      current weight against the required weight.
 *   3. Each skill's "contribution" is currentWeight / requiredWeight, capped at 1.0
 *      (exceeding the requirement still only counts as fully meeting it).
 *      A skill the user does not have at all contributes 0.
 *   4. The overall career readiness percentage is the average of every required
 *      skill's contribution, expressed as a percentage.
 *   5. Each skill is also labelled:
 *        - STRONG              -> current weight >= required weight
 *        - NEEDS_IMPROVEMENT    -> user has the skill, but below the required level
 *        - MISSING              -> user does not have the skill at all
 */
@Service
@RequiredArgsConstructor
public class SkillGapService {

    private final CareerRoleRepository careerRoleRepository;
    private final UserCareerGoalRepository userCareerGoalRepository;
    private final UserSkillRepository userSkillRepository;

    public Optional<SkillGapReportDto> getReportForUser(Long userId) {
        return userCareerGoalRepository.findByUser_Id(userId)
                .map(goal -> buildReport(userId, goal.getCareerRole()));
    }

    public SkillGapReportDto getReportForRole(Long userId, Long careerRoleId) {
        CareerRole role = careerRoleRepository.findById(careerRoleId)
                .orElseThrow(() -> new ResourceNotFoundException("Career role not found"));
        return buildReport(userId, role);
    }

    private SkillGapReportDto buildReport(Long userId, CareerRole role) {
        List<UserSkill> userSkills = userSkillRepository.findByUser_IdOrderBySkill_NameAsc(userId);
        Map<Long, UserSkill> bySkillId = new java.util.HashMap<>();
        for (UserSkill us : userSkills) {
            bySkillId.put(us.getSkill().getId(), us);
        }

        List<SkillGapItemDto> items = new ArrayList<>();
        List<String> strong = new ArrayList<>();
        List<String> improve = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        double totalContribution = 0.0;
        int requiredCount = role.getRequiredSkills().size();

        for (CareerRoleSkill required : role.getRequiredSkills()) {
            int requiredWeight = required.getRequiredProficiency().getWeight();
            UserSkill current = bySkillId.get(required.getSkill().getId());
            int currentWeight = current == null ? 0 : current.getProficiencyLevel().getWeight();
            String currentLevelName = current == null ? "NONE" : current.getProficiencyLevel().name();

            double contribution = Math.min(1.0, currentWeight / (double) requiredWeight);
            totalContribution += contribution;

            String status;
            if (currentWeight == 0) {
                status = "MISSING";
                missing.add(required.getSkill().getName());
            } else if (currentWeight >= requiredWeight) {
                status = "STRONG";
                strong.add(required.getSkill().getName());
            } else {
                status = "NEEDS_IMPROVEMENT";
                improve.add(required.getSkill().getName());
            }

            items.add(new SkillGapItemDto(
                    required.getSkill().getId(),
                    required.getSkill().getName(),
                    required.getRequiredProficiency().name(),
                    requiredWeight,
                    currentLevelName,
                    currentWeight,
                    Math.round(contribution * 1000.0) / 1000.0,
                    status
            ));
        }

        double readiness = requiredCount == 0 ? 0.0 : Math.round((totalContribution / requiredCount) * 1000.0) / 10.0;

        return new SkillGapReportDto(role.getId(), role.getName(), readiness, items, strong, improve, missing);
    }
}
