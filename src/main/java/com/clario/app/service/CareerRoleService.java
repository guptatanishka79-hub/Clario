package com.clario.app.service;

import com.clario.app.dto.*;
import com.clario.app.entity.*;
import com.clario.app.exception.DuplicateResourceException;
import com.clario.app.exception.ResourceNotFoundException;
import com.clario.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CareerRoleService {

    private final CareerRoleRepository careerRoleRepository;
    private final CareerRoleSkillRepository careerRoleSkillRepository;
    private final SkillRepository skillRepository;
    private final UserCareerGoalRepository userCareerGoalRepository;
    private final ActivityService activityService;

    public List<CareerRoleDto> listRoles() {
        return careerRoleRepository.findAll().stream().map(this::toDto).toList();
    }

    public CareerRoleDto getRole(Long id) {
        CareerRole role = careerRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Career role not found"));
        return toDto(role);
    }

    public Optional<CareerRoleDto> getUserGoal(Long userId) {
        return userCareerGoalRepository.findByUser_Id(userId).map(g -> toDto(g.getCareerRole()));
    }

    @Transactional
    public CareerRoleDto setUserGoal(User user, Long careerRoleId) {
        CareerRole role = careerRoleRepository.findById(careerRoleId)
                .orElseThrow(() -> new ResourceNotFoundException("Career role not found"));

        UserCareerGoal goal = userCareerGoalRepository.findByUser_Id(user.getId())
                .orElse(UserCareerGoal.builder().user(user).build());
        goal.setCareerRole(role);
        userCareerGoalRepository.save(goal);

        activityService.log(user, "Set career goal to " + role.getName());
        return toDto(role);
    }

    // ---------- Admin management ----------

    @Transactional
    public CareerRoleDto createRole(CareerRoleRequest request) {
        careerRoleRepository.findByNameIgnoreCase(request.getName()).ifPresent(r -> {
            throw new DuplicateResourceException("A career role named \"" + request.getName() + "\" already exists");
        });
        CareerRole role = CareerRole.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .build();
        role = careerRoleRepository.save(role);
        return toDto(role);
    }

    @Transactional
    public CareerRoleDto updateRole(Long id, CareerRoleRequest request) {
        CareerRole role = careerRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Career role not found"));
        role.setName(request.getName().trim());
        role.setDescription(request.getDescription());
        return toDto(careerRoleRepository.save(role));
    }

    @Transactional
    public void deleteRole(Long id) {
        CareerRole role = careerRoleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Career role not found"));
        careerRoleRepository.delete(role);
    }

    @Transactional
    public CareerRoleDto addRequiredSkill(Long careerRoleId, RequiredSkillRequest request) {
        CareerRole role = careerRoleRepository.findById(careerRoleId)
                .orElseThrow(() -> new ResourceNotFoundException("Career role not found"));
        Skill skill = skillRepository.findById(request.getSkillId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        ProficiencyLevel level = SkillService.parseLevel(request.getRequiredProficiency());

        CareerRoleSkill link = careerRoleSkillRepository.findByCareerRole_IdAndSkill_Id(careerRoleId, skill.getId())
                .orElse(CareerRoleSkill.builder().careerRole(role).skill(skill).build());
        link.setRequiredProficiency(level);
        careerRoleSkillRepository.save(link);

        return toDto(careerRoleRepository.findById(careerRoleId).orElseThrow());
    }

    @Transactional
    public void removeRequiredSkill(Long careerRoleId, Long skillId) {
        careerRoleSkillRepository.deleteByCareerRole_IdAndSkill_Id(careerRoleId, skillId);
    }

    private CareerRoleDto toDto(CareerRole role) {
        List<RequiredSkillDto> required = role.getRequiredSkills().stream()
                .map(rs -> new RequiredSkillDto(
                        rs.getSkill().getId(),
                        rs.getSkill().getName(),
                        rs.getRequiredProficiency().name(),
                        rs.getRequiredProficiency().getWeight()))
                .toList();
        return new CareerRoleDto(role.getId(), role.getName(), role.getDescription(), required);
    }
}
