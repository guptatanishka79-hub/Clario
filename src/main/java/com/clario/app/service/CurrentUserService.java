package com.clario.app.service;

import com.clario.app.entity.User;
import com.clario.app.exception.ResourceNotFoundException;
import com.clario.app.repository.UserRepository;
import com.clario.app.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserDetails principal)) {
            throw new ResourceNotFoundException("No authenticated user in context");
        }
        return userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user no longer exists"));
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserDetails principal)) {
            throw new ResourceNotFoundException("No authenticated user in context");
        }
        return principal.getId();
    }
}
