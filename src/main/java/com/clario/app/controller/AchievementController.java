package com.clario.app.controller;

import com.clario.app.dto.AchievementDto;
import com.clario.app.dto.AchievementRequest;
import com.clario.app.entity.User;
import com.clario.app.service.AchievementService;
import com.clario.app.service.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public List<AchievementDto> list() {
        return achievementService.listForUser(currentUserService.getCurrentUserId());
    }

    @PostMapping
    public ResponseEntity<AchievementDto> add(@Valid @RequestBody AchievementRequest request) {
        User user = currentUserService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(achievementService.add(user, request));
    }

    @PutMapping("/{id}")
    public AchievementDto update(@PathVariable Long id, @Valid @RequestBody AchievementRequest request) {
        User user = currentUserService.getCurrentUser();
        return achievementService.update(user, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        User user = currentUserService.getCurrentUser();
        achievementService.delete(user, id);
        return ResponseEntity.ok(Map.of("message", "Achievement removed"));
    }
}
