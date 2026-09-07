package com.clario.app.service;

import com.clario.app.dto.AchievementDto;
import com.clario.app.dto.AchievementRequest;
import com.clario.app.entity.Achievement;
import com.clario.app.entity.User;
import com.clario.app.exception.ResourceNotFoundException;
import com.clario.app.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final ActivityService activityService;

    public List<AchievementDto> listForUser(Long userId) {
        return achievementRepository.findByUser_IdOrderByAchievementDateDesc(userId).stream().map(this::toDto).toList();
    }

    @Transactional
    public AchievementDto add(User user, AchievementRequest request) {
        Achievement achievement = Achievement.builder()
                .user(user)
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .achievementDate(request.getAchievementDate())
                .organization(request.getOrganization())
                .build();
        achievement = achievementRepository.save(achievement);
        activityService.log(user, "Added achievement: " + achievement.getTitle());
        return toDto(achievement);
    }

    @Transactional
    public AchievementDto update(User user, Long id, AchievementRequest request) {
        Achievement achievement = achievementRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found"));
        achievement.setTitle(request.getTitle().trim());
        achievement.setDescription(request.getDescription());
        achievement.setAchievementDate(request.getAchievementDate());
        achievement.setOrganization(request.getOrganization());
        achievement = achievementRepository.save(achievement);
        activityService.log(user, "Updated achievement: " + achievement.getTitle());
        return toDto(achievement);
    }

    @Transactional
    public void delete(User user, Long id) {
        Achievement achievement = achievementRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Achievement not found"));
        achievementRepository.delete(achievement);
        activityService.log(user, "Removed achievement: " + achievement.getTitle());
    }

    private AchievementDto toDto(Achievement a) {
        return new AchievementDto(a.getId(), a.getTitle(), a.getDescription(), a.getAchievementDate(), a.getOrganization());
    }
}
