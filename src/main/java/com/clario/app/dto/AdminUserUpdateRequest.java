package com.clario.app.dto;

import lombok.Data;

@Data
public class AdminUserUpdateRequest {
    private Boolean enabled;
    private String role; // ROLE_USER or ROLE_ADMIN
}
