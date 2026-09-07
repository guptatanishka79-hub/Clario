package com.clario.app.repository;

import com.clario.app.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    List<UserSkill> findByUser_IdOrderBySkill_NameAsc(Long userId);
    Optional<UserSkill> findByIdAndUser_Id(Long id, Long userId);
    Optional<UserSkill> findByUser_IdAndSkill_Id(Long userId, Long skillId);
    long countByUser_Id(Long userId);

    @org.springframework.data.jpa.repository.Query(
        "select us.skill.name as name, count(us) as total from UserSkill us group by us.skill.name order by total desc")
    List<Object[]> findMostPopularSkillsRaw();
}
