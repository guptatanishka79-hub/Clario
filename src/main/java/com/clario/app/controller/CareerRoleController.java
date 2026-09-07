package com.clario.app.controller;

import com.clario.app.dto.CareerGoalRequest;
import com.clario.app.dto.CareerRoleDto;
import com.clario.app.entity.User;
import com.clario.app.exception.ResourceNotFoundException;
import com.clario.app.service.CareerRoleService;
import com.clario.app.service.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/career-roles")
@RequiredArgsConstructor
public class CareerRoleController {

    private final CareerRoleService careerRoleService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public List<CareerRoleDto> list() {
        return careerRoleService.listRoles();
    }

    @GetMapping("/{id}")
    public CareerRoleDto get(@PathVariable Long id) {
        return careerRoleService.getRole(id);
    }

    @GetMapping("/goal")
    public CareerRoleDto getMyGoal() {
        return careerRoleService.getUserGoal(currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("No career goal set yet"));
    }

    @PutMapping("/goal")
    public CareerRoleDto setMyGoal(@Valid @RequestBody CareerGoalRequest request) {
        User user = currentUserService.getCurrentUser();
        return careerRoleService.setUserGoal(user, request.getCareerRoleId());
    }
}
