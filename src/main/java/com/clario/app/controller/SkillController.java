package com.clario.app.controller;

import com.clario.app.dto.*;
import com.clario.app.entity.User;
import com.clario.app.service.CurrentUserService;
import com.clario.app.service.SkillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;
    private final CurrentUserService currentUserService;

    @GetMapping("/categories")
    public List<SkillCategoryDto> categories() {
        return skillService.listCategories();
    }

    @GetMapping("/catalog")
    public List<SkillDto> catalog(@RequestParam(required = false) String query,
                                   @RequestParam(required = false) Long categoryId) {
        return skillService.listCatalog(query, categoryId);
    }

    @GetMapping
    public List<UserSkillDto> mySkills(@RequestParam(required = false) String query,
                                        @RequestParam(required = false) Long categoryId) {
        return skillService.listUserSkills(currentUserService.getCurrentUserId(), query, categoryId);
    }

    @PostMapping
    public ResponseEntity<UserSkillDto> addSkill(@Valid @RequestBody UserSkillRequest request) {
        User user = currentUserService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(skillService.addUserSkill(user, request));
    }

    @PutMapping("/{id}")
    public UserSkillDto updateSkill(@PathVariable Long id, @Valid @RequestBody UserSkillRequest request) {
        User user = currentUserService.getCurrentUser();
        return skillService.updateUserSkill(user, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSkill(@PathVariable Long id) {
        User user = currentUserService.getCurrentUser();
        skillService.deleteUserSkill(user, id);
        return ResponseEntity.ok(Map.of("message", "Skill removed"));
    }
}
