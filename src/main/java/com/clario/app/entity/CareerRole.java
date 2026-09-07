package com.clario.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "career_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(length = 500)
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "careerRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CareerRoleSkill> requiredSkills = new ArrayList<>();
}
