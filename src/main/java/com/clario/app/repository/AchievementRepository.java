package com.clario.app.repository;

import com.clario.app.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByUser_IdOrderByAchievementDateDesc(Long userId);
    Optional<Achievement> findByIdAndUser_Id(Long id, Long userId);
}
