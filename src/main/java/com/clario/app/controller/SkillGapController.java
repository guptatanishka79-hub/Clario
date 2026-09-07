package com.clario.app.controller;

import com.clario.app.dto.SkillGapReportDto;
import com.clario.app.exception.ResourceNotFoundException;
import com.clario.app.service.CurrentUserService;
import com.clario.app.service.SkillGapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skill-gap")
@RequiredArgsConstructor
public class SkillGapController {

    private final SkillGapService skillGapService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public SkillGapReportDto myGap() {
        return skillGapService.getReportForUser(currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Set a career goal first to see your skill-gap analysis"));
    }

    @GetMapping("/preview/{careerRoleId}")
    public SkillGapReportDto preview(@PathVariable Long careerRoleId) {
        return skillGapService.getReportForRole(currentUserService.getCurrentUserId(), careerRoleId);
    }
}
