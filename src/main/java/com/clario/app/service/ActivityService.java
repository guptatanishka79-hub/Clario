package com.clario.app.service;

import com.clario.app.dto.ActivityLogDto;
import com.clario.app.entity.ActivityLog;
import com.clario.app.entity.User;
import com.clario.app.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityLogRepository activityLogRepository;

    public void log(User user, String description) {
        ActivityLog entry = ActivityLog.builder()
                .user(user)
                .description(description)
                .build();
        activityLogRepository.save(entry);
    }

    public List<ActivityLogDto> recentForUser(Long userId, int limit) {
        return activityLogRepository.findByUser_IdOrderByCreatedAtDesc(userId, PageRequest.of(0, limit))
                .stream()
                .map(a -> new ActivityLogDto(a.getId(), a.getDescription(), a.getCreatedAt()))
                .toList();
    }
}
