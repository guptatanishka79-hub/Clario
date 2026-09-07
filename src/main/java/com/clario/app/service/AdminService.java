package com.clario.app.service;

import com.clario.app.dto.*;
import com.clario.app.entity.Role;
import com.clario.app.entity.User;
import com.clario.app.exception.BadRequestException;
import com.clario.app.exception.ResourceNotFoundException;
import com.clario.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SkillRepository skillRepository;
    private final CareerRoleRepository careerRoleRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserCareerGoalRepository userCareerGoalRepository;

    public List<UserSummaryDto> listUsers() {
        return userRepository.findAll().stream()
                .map(u -> new UserSummaryDto(u.getId(), u.getFullName(), u.getEmail(), u.getRole().getName(), u.isEnabled()))
                .toList();
    }

    @Transactional
    public UserSummaryDto updateUser(Long userId, AdminUserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getEnabled() != null) {
            user.setEnabled(request.getEnabled());
        }
        if (request.getRole() != null && !request.getRole().isBlank()) {
            if (!request.getRole().equals("ROLE_USER") && !request.getRole().equals("ROLE_ADMIN")) {
                throw new BadRequestException("Role must be ROLE_USER or ROLE_ADMIN");
            }
            Role role = roleRepository.findByName(request.getRole())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not configured"));
            user.setRole(role);
        }
        user = userRepository.save(user);
        return new UserSummaryDto(user.getId(), user.getFullName(), user.getEmail(), user.getRole().getName(), user.isEnabled());
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }

    public AdminStatsDto getStats() {
        long totalUsers = userRepository.countByRole_Name("ROLE_USER");
        long totalAdmins = userRepository.countByRole_Name("ROLE_ADMIN");
        long totalSkills = skillRepository.count();
        long totalCareerRoles = careerRoleRepository.count();

        List<PopularSkillDto> popular = userSkillRepository.findMostPopularSkillsRaw().stream()
                .limit(10)
                .map(row -> new PopularSkillDto((String) row[0], (Long) row[1]))
                .toList();

        List<UsersByRoleDto> byRole = userCareerGoalRepository.findUsersByTargetRoleRaw().stream()
                .map(row -> new UsersByRoleDto((String) row[0], (Long) row[1]))
                .toList();

        return new AdminStatsDto(totalUsers, totalAdmins, totalSkills, totalCareerRoles, popular, byRole);
    }
}
