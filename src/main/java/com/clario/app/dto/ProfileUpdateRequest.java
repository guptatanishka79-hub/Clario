package com.clario.app.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProfileUpdateRequest {
    @Size(max = 120)
    private String fullName;
    @Size(max = 30)
    private String phone;
    @Size(max = 120)
    private String location;
    @Size(max = 150)
    private String headline;
    @Size(max = 1000)
    private String bio;
    private Integer yearsOfExperience;
    @Size(max = 120)
    private String currentJobTitle;
}
