package com.clario.app.repository;

import com.clario.app.entity.UserCareerGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCareerGoalRepository extends JpaRepository<UserCareerGoal, Long> {
    Optional<UserCareerGoal> findByUser_Id(Long userId);

    @org.springframework.data.jpa.repository.Query(
        "select g.careerRole.name as roleName, count(g) as total from UserCareerGoal g group by g.careerRole.name order by total desc")
    List<Object[]> findUsersByTargetRoleRaw();
}
