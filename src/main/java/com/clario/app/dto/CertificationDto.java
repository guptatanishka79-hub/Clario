package com.clario.app.dto;

import java.time.LocalDate;

public record CertificationDto(
        Long id, String name, String issuingOrganization, LocalDate issueDate,
        LocalDate expiryDate, String credentialId, String credentialUrl, boolean expired
) {
}
