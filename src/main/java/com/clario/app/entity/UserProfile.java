package com.clario.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 30)
    private String phone;

    @Column(length = 120)
    private String location;

    @Column(length = 150)
    private String headline;

    @Column(length = 1000)
    private String bio;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "current_job_title", length = 120)
    private String currentJobTitle;
}
