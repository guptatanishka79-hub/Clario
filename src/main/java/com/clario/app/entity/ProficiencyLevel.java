package com.clario.app.entity;

/**
 * Proficiency scale used across user skills and career-role requirements.
 * The numeric weight backs the skill-gap calculation (see SkillGapService).
 */
public enum ProficiencyLevel {
    BEGINNER(1),
    INTERMEDIATE(2),
    ADVANCED(3),
    EXPERT(4);

    private final int weight;

    ProficiencyLevel(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
