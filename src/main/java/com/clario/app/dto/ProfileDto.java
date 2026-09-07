package com.clario.app.dto;

public record ProfileDto(
        Long userId,
        String fullName,
        String email,
        String phone,
        String location,
        String headline,
        String bio,
        Integer yearsOfExperience,
        String currentJobTitle,
        int profileCompletionPercent
) {
}
