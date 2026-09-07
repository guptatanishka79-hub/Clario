package com.clario.app.service;

import com.clario.app.dto.ProfileDto;
import com.clario.app.dto.ProfileUpdateRequest;
import com.clario.app.entity.User;
import com.clario.app.entity.UserProfile;
import com.clario.app.repository.UserProfileRepository;
import com.clario.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;

    private UserProfile getOrCreateProfile(User user) {
        return userProfileRepository.findByUser_Id(user.getId())
                .orElseGet(() -> userProfileRepository.save(UserProfile.builder().user(user).build()));
    }

    public ProfileDto getProfile(User user) {
        UserProfile profile = getOrCreateProfile(user);
        return toDto(user, profile);
    }

    @Transactional
    public ProfileDto updateProfile(User user, ProfileUpdateRequest request) {
        UserProfile profile = getOrCreateProfile(user);

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
            userRepository.save(user);
        }
        profile.setPhone(request.getPhone());
        profile.setLocation(request.getLocation());
        profile.setHeadline(request.getHeadline());
        profile.setBio(request.getBio());
        profile.setYearsOfExperience(request.getYearsOfExperience());
        profile.setCurrentJobTitle(request.getCurrentJobTitle());
        userProfileRepository.save(profile);

        activityService.log(user, "Updated profile information");

        return toDto(user, profile);
    }

    /**
     * Profile completion is calculated from eight fields: full name, email,
     * phone, location, headline, bio, years of experience, and current job title.
     * Each populated field contributes an equal share of the 100%.
     */
    public static int calculateCompletion(User user, UserProfile profile) {
        int total = 8;
        int filled = 0;
        if (user.getFullName() != null && !user.getFullName().isBlank()) filled++;
        if (user.getEmail() != null && !user.getEmail().isBlank()) filled++;
        if (profile.getPhone() != null && !profile.getPhone().isBlank()) filled++;
        if (profile.getLocation() != null && !profile.getLocation().isBlank()) filled++;
        if (profile.getHeadline() != null && !profile.getHeadline().isBlank()) filled++;
        if (profile.getBio() != null && !profile.getBio().isBlank()) filled++;
        if (profile.getYearsOfExperience() != null) filled++;
        if (profile.getCurrentJobTitle() != null && !profile.getCurrentJobTitle().isBlank()) filled++;
        return (int) Math.round((filled * 100.0) / total);
    }

    private ProfileDto toDto(User user, UserProfile profile) {
        return new ProfileDto(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                profile.getPhone(),
                profile.getLocation(),
                profile.getHeadline(),
                profile.getBio(),
                profile.getYearsOfExperience(),
                profile.getCurrentJobTitle(),
                calculateCompletion(user, profile)
        );
    }
}
