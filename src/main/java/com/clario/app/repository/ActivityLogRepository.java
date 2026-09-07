package com.clario.app.repository;

import com.clario.app.entity.ActivityLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByUser_IdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
