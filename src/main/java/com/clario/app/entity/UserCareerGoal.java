package com.clario.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "user_career_goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCareerGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "career_role_id", nullable = false)
    private CareerRole careerRole;

    @Column(name = "set_date", nullable = false)
    private LocalDate setDate;

    @PrePersist
    @PreUpdate
    protected void touch() {
        this.setDate = LocalDate.now();
    }
}
