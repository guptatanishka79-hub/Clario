package com.clario.app.dto;

import java.time.LocalDateTime;

public record ActivityLogDto(Long id, String description, LocalDateTime createdAt) {
}
