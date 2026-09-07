package com.clario.app.repository;

import com.clario.app.entity.SkillProgressHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillProgressHistoryRepository extends JpaRepository<SkillProgressHistory, Long> {
    List<SkillProgressHistory> findByUserSkill_IdOrderByRecordedDateAsc(Long userSkillId);
    List<SkillProgressHistory> findByUserSkill_User_IdOrderByRecordedDateAsc(Long userId);
}
