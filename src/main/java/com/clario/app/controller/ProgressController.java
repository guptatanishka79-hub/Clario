package com.clario.app.controller;

import com.clario.app.dto.SkillProgressDto;
import com.clario.app.service.CurrentUserService;
import com.clario.app.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public List<SkillProgressDto> myProgress() {
        return progressService.getProgressForUser(currentUserService.getCurrentUserId());
    }

    @GetMapping("/{userSkillId}")
    public List<SkillProgressDto> forSkill(@PathVariable Long userSkillId) {
        return progressService.getProgressForUserSkill(currentUserService.getCurrentUserId(), userSkillId);
    }
}
