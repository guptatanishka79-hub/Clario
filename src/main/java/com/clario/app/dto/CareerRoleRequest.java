package com.clario.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CareerRoleRequest {
    @NotBlank(message = "Role name is required")
    private String name;
    private String description;
}
