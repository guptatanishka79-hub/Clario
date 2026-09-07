package com.clario.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_skills", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "skill_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level", nullable = false, length = 20)
    private ProficiencyLevel proficiencyLevel;

    @Column(name = "years_of_experience")
    private Double yearsOfExperience;

    @Column(name = "last_updated", nullable = false)
    private LocalDate lastUpdated;

    @Builder.Default
    @OneToMany(mappedBy = "userSkill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SkillProgressHistory> history = new ArrayList<>();

    @PrePersist
    @PreUpdate
    protected void touch() {
        this.lastUpdated = LocalDate.now();
    }
}
