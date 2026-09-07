package com.clario.app.controller;

import com.clario.app.dto.RegisterRequest;
import com.clario.app.dto.UserSummaryDto;
import com.clario.app.security.AppUserDetails;
import com.clario.app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * NOTE: /api/auth/login and /api/auth/logout are handled directly by Spring
 * Security's formLogin()/logout() filters configured in SecurityConfig — they
 * never reach this controller. Only registration and "who am I" live here.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserSummaryDto> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserDetails principal)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not authenticated"));
        }
        UserSummaryDto dto = new UserSummaryDto(
                principal.getId(),
                principal.getUser().getFullName(),
                principal.getUsername(),
                principal.getUser().getRole().getName(),
                principal.getUser().isEnabled()
        );
        return ResponseEntity.ok(dto);
    }
}
