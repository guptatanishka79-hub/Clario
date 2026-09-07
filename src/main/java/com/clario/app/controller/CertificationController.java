package com.clario.app.controller;

import com.clario.app.dto.CertificationDto;
import com.clario.app.dto.CertificationRequest;
import com.clario.app.entity.User;
import com.clario.app.service.CertificationService;
import com.clario.app.service.CurrentUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certifications")
@RequiredArgsConstructor
public class CertificationController {

    private final CertificationService certificationService;
    private final CurrentUserService currentUserService;

    @GetMapping
    public List<CertificationDto> list() {
        return certificationService.listForUser(currentUserService.getCurrentUserId());
    }

    @PostMapping
    public ResponseEntity<CertificationDto> add(@Valid @RequestBody CertificationRequest request) {
        User user = currentUserService.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(certificationService.add(user, request));
    }

    @PutMapping("/{id}")
    public CertificationDto update(@PathVariable Long id, @Valid @RequestBody CertificationRequest request) {
        User user = currentUserService.getCurrentUser();
        return certificationService.update(user, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        User user = currentUserService.getCurrentUser();
        certificationService.delete(user, id);
        return ResponseEntity.ok(Map.of("message", "Certification removed"));
    }
}
