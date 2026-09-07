package com.clario.app.dto;

public record UserSummaryDto(Long id, String fullName, String email, String role, boolean enabled) {
}
