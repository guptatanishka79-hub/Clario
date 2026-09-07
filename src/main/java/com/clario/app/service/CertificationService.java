package com.clario.app.service;

import com.clario.app.dto.CertificationDto;
import com.clario.app.dto.CertificationRequest;
import com.clario.app.entity.Certification;
import com.clario.app.entity.User;
import com.clario.app.exception.ResourceNotFoundException;
import com.clario.app.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationService {

    private final CertificationRepository certificationRepository;
    private final ActivityService activityService;

    public List<CertificationDto> listForUser(Long userId) {
        return certificationRepository.findByUser_IdOrderByIssueDateDesc(userId).stream().map(this::toDto).toList();
    }

    @Transactional
    public CertificationDto add(User user, CertificationRequest request) {
        Certification cert = Certification.builder()
                .user(user)
                .name(request.getName().trim())
                .issuingOrganization(request.getIssuingOrganization())
                .issueDate(request.getIssueDate())
                .expiryDate(request.getExpiryDate())
                .credentialId(request.getCredentialId())
                .credentialUrl(request.getCredentialUrl())
                .build();
        cert = certificationRepository.save(cert);
        activityService.log(user, "Added " + cert.getName() + " certification");
        return toDto(cert);
    }

    @Transactional
    public CertificationDto update(User user, Long id, CertificationRequest request) {
        Certification cert = certificationRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found"));
        cert.setName(request.getName().trim());
        cert.setIssuingOrganization(request.getIssuingOrganization());
        cert.setIssueDate(request.getIssueDate());
        cert.setExpiryDate(request.getExpiryDate());
        cert.setCredentialId(request.getCredentialId());
        cert.setCredentialUrl(request.getCredentialUrl());
        cert = certificationRepository.save(cert);
        activityService.log(user, "Updated " + cert.getName() + " certification");
        return toDto(cert);
    }

    @Transactional
    public void delete(User user, Long id) {
        Certification cert = certificationRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Certification not found"));
        certificationRepository.delete(cert);
        activityService.log(user, "Removed " + cert.getName() + " certification");
    }

    private CertificationDto toDto(Certification c) {
        boolean expired = c.getExpiryDate() != null && c.getExpiryDate().isBefore(LocalDate.now());
        return new CertificationDto(c.getId(), c.getName(), c.getIssuingOrganization(), c.getIssueDate(),
                c.getExpiryDate(), c.getCredentialId(), c.getCredentialUrl(), expired);
    }
}
