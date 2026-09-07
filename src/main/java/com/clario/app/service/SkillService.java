package com.clario.app.service;

import com.clario.app.dto.*;
import com.clario.app.entity.*;
import com.clario.app.exception.BadRequestException;
import com.clario.app.exception.DuplicateResourceException;
import com.clario.app.exception.ResourceNotFoundException;
import com.clario.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillCategoryRepository skillCategoryRepository;
    private final UserSkillRepository userSkillRepository;
    private final SkillProgressHistoryRepository progressHistoryRepository;
    private final ActivityService activityService;

    // ---------- Catalog (used for dropdowns, admin, search) ----------

    public List<SkillCategoryDto> listCategories() {
        return skillCategoryRepository.findAll().stream()
                .map(c -> new SkillCategoryDto(c.getId(), c.getName()))
                .toList();
    }

    public List<SkillDto> listCatalog(String query, Long categoryId) {
        List<Skill> skills;
        if (query != null && !query.isBlank()) {
            skills = skillRepository.findByNameContainingIgnoreCase(query.trim());
        } else if (categoryId != null) {
            skills = skillRepository.findByCategory_Id(categoryId);
        } else {
            skills = skillRepository.findAll();
        }
        return skills.stream().map(this::toSkillDto).toList();
    }

    private SkillDto toSkillDto(Skill s) {
        return new SkillDto(s.getId(), s.getName(), s.getCategory().getId(), s.getCategory().getName());
    }

    // ---------- Admin catalog management ----------

    @Transactional
    public SkillCategoryDto createCategory(String name) {
        skillCategoryRepository.findByNameIgnoreCase(name).ifPresent(c -> {
            throw new DuplicateResourceException("Category \"" + name + "\" already exists");
        });
        SkillCategory category = skillCategoryRepository.save(SkillCategory.builder().name(name.trim()).build());
        return new SkillCategoryDto(category.getId(), category.getName());
    }

    @Transactional
    public void deleteCategory(Long id) {
        SkillCategory category = skillCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        skillCategoryRepository.delete(category);
    }

    @Transactional
    public SkillDto createSkill(String name, Long categoryId) {
        skillRepository.findByNameIgnoreCase(name).ifPresent(s -> {
            throw new DuplicateResourceException("Skill \"" + name + "\" already exists");
        });
        SkillCategory category = skillCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Skill skill = skillRepository.save(Skill.builder().name(name.trim()).category(category).build());
        return toSkillDto(skill);
    }

    @Transactional
    public SkillDto updateSkill(Long id, String name, Long categoryId) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        if (name != null && !name.isBlank()) {
            skill.setName(name.trim());
        }
        if (categoryId != null) {
            SkillCategory category = skillCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            skill.setCategory(category);
        }
        return toSkillDto(skillRepository.save(skill));
    }

    @Transactional
    public void deleteSkill(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        skillRepository.delete(skill);
    }

    // ---------- User's own skills ----------

    public List<UserSkillDto> listUserSkills(Long userId, String query, Long categoryId) {
        List<UserSkill> skills = userSkillRepository.findByUser_IdOrderBySkill_NameAsc(userId);
        return skills.stream()
                .filter(us -> query == null || query.isBlank() || us.getSkill().getName().toLowerCase().contains(query.toLowerCase()))
                .filter(us -> categoryId == null || us.getSkill().getCategory().getId().equals(categoryId))
                .map(this::toUserSkillDto)
                .toList();
    }

    @Transactional
    public UserSkillDto addUserSkill(User user, UserSkillRequest request) {
        Skill skill = resolveSkill(request);
        ProficiencyLevel level = parseLevel(request.getProficiencyLevel());

        if (userSkillRepository.findByUser_IdAndSkill_Id(user.getId(), skill.getId()).isPresent()) {
            throw new DuplicateResourceException("You already have \"" + skill.getName() + "\" in your skill list. Edit it instead.");
        }

        UserSkill userSkill = UserSkill.builder()
                .user(user)
                .skill(skill)
                .proficiencyLevel(level)
                .yearsOfExperience(request.getYearsOfExperience())
                .lastUpdated(LocalDate.now())
                .build();
        userSkill = userSkillRepository.save(userSkill);
        recordHistory(userSkill, level);

        activityService.log(user, "Added " + skill.getName() + " skill (" + level.name() + ")");
        return toUserSkillDto(userSkill);
    }

    @Transactional
    public UserSkillDto updateUserSkill(User user, Long userSkillId, UserSkillRequest request) {
        UserSkill userSkill = userSkillRepository.findByIdAndUser_Id(userSkillId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill entry not found"));

        ProficiencyLevel newLevel = parseLevel(request.getProficiencyLevel());
        boolean levelChanged = newLevel != userSkill.getProficiencyLevel();

        userSkill.setProficiencyLevel(newLevel);
        if (request.getYearsOfExperience() != null) {
            userSkill.setYearsOfExperience(request.getYearsOfExperience());
        }
        userSkill = userSkillRepository.save(userSkill);

        if (levelChanged) {
            recordHistory(userSkill, newLevel);
            activityService.log(user, "Updated " + userSkill.getSkill().getName() + " proficiency to " + newLevel.name());
        }

        return toUserSkillDto(userSkill);
    }

    @Transactional
    public void deleteUserSkill(User user, Long userSkillId) {
        UserSkill userSkill = userSkillRepository.findByIdAndUser_Id(userSkillId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Skill entry not found"));
        String name = userSkill.getSkill().getName();
        userSkillRepository.delete(userSkill);
        activityService.log(user, "Removed " + name + " from skills");
    }

    private void recordHistory(UserSkill userSkill, ProficiencyLevel level) {
        SkillProgressHistory history = SkillProgressHistory.builder()
                .userSkill(userSkill)
                .proficiencyLevel(level)
                .recordedDate(LocalDate.now())
                .build();
        progressHistoryRepository.save(history);
    }

    private Skill resolveSkill(UserSkillRequest request) {
        if (request.getSkillId() != null) {
            return skillRepository.findById(request.getSkillId())
                    .orElseThrow(() -> new ResourceNotFoundException("Skill not found"));
        }
        if (request.getSkillName() == null || request.getSkillName().isBlank()) {
            throw new BadRequestException("Either skillId or skillName must be provided");
        }
        return skillRepository.findByNameIgnoreCase(request.getSkillName().trim())
                .orElseGet(() -> {
                    SkillCategory category = request.getCategoryId() != null
                            ? skillCategoryRepository.findById(request.getCategoryId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"))
                            : skillCategoryRepository.findByNameIgnoreCase("Tools")
                                    .orElseThrow(() -> new ResourceNotFoundException("Default category not configured"));
                    Skill newSkill = Skill.builder().name(request.getSkillName().trim()).category(category).build();
                    return skillRepository.save(newSkill);
                });
    }

    public static ProficiencyLevel parseLevel(String raw) {
        try {
            return ProficiencyLevel.valueOf(raw.trim().toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Invalid proficiency level: " + raw);
        }
    }

    private UserSkillDto toUserSkillDto(UserSkill us) {
        return new UserSkillDto(
                us.getId(),
                us.getSkill().getId(),
                us.getSkill().getName(),
                us.getSkill().getCategory().getId(),
                us.getSkill().getCategory().getName(),
                us.getProficiencyLevel().name(),
                us.getProficiencyLevel().getWeight(),
                us.getYearsOfExperience(),
                us.getLastUpdated()
        );
    }
}
