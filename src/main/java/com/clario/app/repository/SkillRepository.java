package com.clario.app.repository;

import com.clario.app.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findByNameIgnoreCase(String name);
    List<Skill> findByCategory_Id(Long categoryId);
    List<Skill> findByNameContainingIgnoreCase(String query);
}
