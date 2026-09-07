package com.clario.app.repository;

import com.clario.app.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByUser_IdOrderByIssueDateDesc(Long userId);
    Optional<Certification> findByIdAndUser_Id(Long id, Long userId);
    long countByUser_Id(Long userId);
}
