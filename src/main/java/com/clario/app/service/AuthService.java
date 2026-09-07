package com.clario.app.service;

import com.clario.app.dto.RegisterRequest;
import com.clario.app.dto.UserSummaryDto;
import com.clario.app.entity.Role;
import com.clario.app.entity.User;
import com.clario.app.entity.UserProfile;
import com.clario.app.exception.BadRequestException;
import com.clario.app.exception.DuplicateResourceException;
import com.clario.app.exception.ResourceNotFoundException;
import com.clario.app.repository.RoleRepository;
import com.clario.app.repository.UserProfileRepository;
import com.clario.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final ActivityService activityService;

    @Transactional
    public UserSummaryDto register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Password and confirm password do not match");
        }
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not configured"));

        User user = User.builder()
                .fullName(request.getFullName().trim())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .enabled(true)
                .build();
        user = userRepository.save(user);

        UserProfile profile = UserProfile.builder()
                .user(user)
                .build();
        userProfileRepository.save(profile);

        activityService.log(user, "Created a Clario account");

        return new UserSummaryDto(user.getId(), user.getFullName(), user.getEmail(), user.getRole().getName(), user.isEnabled());
    }
}
