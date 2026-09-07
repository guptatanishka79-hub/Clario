package com.clario.app.service;

import com.clario.app.dto.SkillProgressDto;
import com.clario.app.dto.SkillProgressPointDto;
import com.clario.app.entity.SkillProgressHistory;
import com.clario.app.entity.UserSkill;
import com.clario.app.repository.SkillProgressHistoryRepository;
import com.clario.app.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final SkillProgressHistoryRepository progressHistoryRepository;
    private final UserSkillRepository userSkillRepository;

    public List<SkillProgressDto> getProgressForUser(Long userId) {
        List<UserSkill> userSkills = userSkillRepository.findByUser_IdOrderBySkill_NameAsc(userId);

        return userSkills.stream()
                .map(us -> {
                    List<SkillProgressPointDto> points = progressHistoryRepository
                            .findByUserSkill_IdOrderByRecordedDateAsc(us.getId())
                            .stream()
                            .map(h -> new SkillProgressPointDto(h.getProficiencyLevel().name(), h.getProficiencyLevel().getWeight(), h.getRecordedDate()))
                            .toList();
                    return new SkillProgressDto(us.getId(), us.getSkill().getName(), points);
                })
                .sorted(Comparator.comparing(SkillProgressDto::skillName))
                .toList();
    }

    public List<SkillProgressDto> getProgressForUserSkill(Long userId, Long userSkillId) {
        UserSkill us = userSkillRepository.findByIdAndUser_Id(userSkillId, userId)
                .orElseThrow(() -> new com.clario.app.exception.ResourceNotFoundException("Skill entry not found"));
        List<SkillProgressPointDto> points = progressHistoryRepository
                .findByUserSkill_IdOrderByRecordedDateAsc(us.getId())
                .stream()
                .map(h -> new SkillProgressPointDto(h.getProficiencyLevel().name(), h.getProficiencyLevel().getWeight(), h.getRecordedDate()))
                .toList();
        return List.of(new SkillProgressDto(us.getId(), us.getSkill().getName(), points));
    }
}
