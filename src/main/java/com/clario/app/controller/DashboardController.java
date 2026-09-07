package com.clario.app.controller;

import com.clario.app.dto.DashboardDto;
import com.clario.app.service.CurrentUserService;
import com.clario.app.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public DashboardDto getDashboard() {
        return dashboardService.buildDashboard(currentUserService.getCurrentUser());
    }
}
