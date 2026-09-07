package com.clario.app.repository;

import com.clario.app.entity.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillCategoryRepository extends JpaRepository<SkillCategory, Long> {
    Optional<SkillCategory> findByNameIgnoreCase(String name);
}
