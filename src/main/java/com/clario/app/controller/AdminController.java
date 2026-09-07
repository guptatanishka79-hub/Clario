package com.clario.app.controller;

import com.clario.app.dto.*;
import com.clario.app.service.AdminService;
import com.clario.app.service.CareerRoleService;
import com.clario.app.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * All endpoints here require ROLE_ADMIN — enforced centrally in SecurityConfig
 * via requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN").
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final SkillService skillService;
    private final CareerRoleService careerRoleService;

    // ---------- Stats ----------

    @GetMapping("/stats")
    public AdminStatsDto stats() {
        return adminService.getStats();
    }

    // ---------- Users ----------

    @GetMapping("/users")
    public List<UserSummaryDto> users() {
        return adminService.listUsers();
    }

    @PutMapping("/users/{id}")
    public UserSummaryDto updateUser(@PathVariable Long id, @RequestBody AdminUserUpdateRequest request) {
        return adminService.updateUser(id, request);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted"));
    }

    // ---------- Skill categories ----------

    @GetMapping("/categories")
    public List<SkillCategoryDto> categories() {
        return skillService.listCategories();
    }

    @PostMapping("/categories")
    public ResponseEntity<SkillCategoryDto> createCategory(@RequestBody Map<String, String> body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.createCategory(body.get("name")));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
        skillService.deleteCategory(id);
        return ResponseEntity.ok(Map.of("message", "Category deleted"));
    }

    // ---------- Skills ----------

    @GetMapping("/skills")
    public List<SkillDto> skills() {
        return skillService.listCatalog(null, null);
    }

    @PostMapping("/skills")
    public ResponseEntity<SkillDto> createSkill(@RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        Long categoryId = Long.valueOf(body.get("categoryId").toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.createSkill(name, categoryId));
    }

    @PutMapping("/skills/{id}")
    public SkillDto updateSkill(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String name = (String) body.get("name");
        Long categoryId = body.get("categoryId") != null ? Long.valueOf(body.get("categoryId").toString()) : null;
        return skillService.updateSkill(id, name, categoryId);
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<Map<String, String>> deleteSkill(@PathVariable Long id) {
        skillService.deleteSkill(id);
        return ResponseEntity.ok(Map.of("message", "Skill deleted"));
    }

    // ---------- Career roles ----------

    @GetMapping("/career-roles")
    public List<CareerRoleDto> careerRoles() {
        return careerRoleService.listRoles();
    }

    @PostMapping("/career-roles")
    public ResponseEntity<CareerRoleDto> createRole(@Valid @RequestBody CareerRoleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(careerRoleService.createRole(request));
    }

    @PutMapping("/career-roles/{id}")
    public CareerRoleDto updateRole(@PathVariable Long id, @Valid @RequestBody CareerRoleRequest request) {
        return careerRoleService.updateRole(id, request);
    }

    @DeleteMapping("/career-roles/{id}")
    public ResponseEntity<Map<String, String>> deleteRole(@PathVariable Long id) {
        careerRoleService.deleteRole(id);
        return ResponseEntity.ok(Map.of("message", "Career role deleted"));
    }

    @PostMapping("/career-roles/{id}/required-skills")
    public CareerRoleDto addRequiredSkill(@PathVariable Long id, @Valid @RequestBody RequiredSkillRequest request) {
        return careerRoleService.addRequiredSkill(id, request);
    }

    @DeleteMapping("/career-roles/{id}/required-skills/{skillId}")
    public CareerRoleDto removeRequiredSkill(@PathVariable Long id, @PathVariable Long skillId) {
        careerRoleService.removeRequiredSkill(id, skillId);
        return careerRoleService.getRole(id);
    }
}
