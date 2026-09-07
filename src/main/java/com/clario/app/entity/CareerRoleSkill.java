package com.clario.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "career_role_skills", uniqueConstraints = @UniqueConstraint(columnNames = {"career_role_id", "skill_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerRoleSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_role_id", nullable = false)
    private CareerRole careerRole;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_proficiency", nullable = false, length = 20)
    private ProficiencyLevel requiredProficiency;
}
