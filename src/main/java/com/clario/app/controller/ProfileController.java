package com.clario.app.controller;

import com.clario.app.dto.ProfileDto;
import com.clario.app.dto.ProfileUpdateRequest;
import com.clario.app.entity.User;
import com.clario.app.service.CurrentUserService;
import com.clario.app.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public ProfileDto getProfile() {
        return profileService.getProfile(currentUserService.getCurrentUser());
    }

    @PutMapping
    public ProfileDto updateProfile(@Valid @RequestBody ProfileUpdateRequest request) {
        User user = currentUserService.getCurrentUser();
        return profileService.updateProfile(user, request);
    }
}
