package com.clario.app.repository;

import com.clario.app.entity.CareerRoleSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CareerRoleSkillRepository extends JpaRepository<CareerRoleSkill, Long> {
    List<CareerRoleSkill> findByCareerRole_Id(Long careerRoleId);
    Optional<CareerRoleSkill> findByCareerRole_IdAndSkill_Id(Long careerRoleId, Long skillId);
    void deleteByCareerRole_IdAndSkill_Id(Long careerRoleId, Long skillId);
}
