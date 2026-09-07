package com.clario.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "skill_progress_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillProgressHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_skill_id", nullable = false)
    private UserSkill userSkill;

    @Enumerated(EnumType.STRING)
    @Column(name = "proficiency_level", nullable = false, length = 20)
    private ProficiencyLevel proficiencyLevel;

    @Column(name = "recorded_date", nullable = false)
    private LocalDate recordedDate;
}
