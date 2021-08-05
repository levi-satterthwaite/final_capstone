package com.techelevator.recipes.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DifficultyLevel {

    public static final DifficultyLevel EASY = new DifficultyLevel("Easy");
    public static final DifficultyLevel MEDIUM = new DifficultyLevel("Medium");
    public static final DifficultyLevel HARD = new DifficultyLevel("Hard");

    private String name;

    public DifficultyLevel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<DifficultyLevel> getDifficultyLevels() {
        List<DifficultyLevel> difficultyLevels = new ArrayList<DifficultyLevel>();
        difficultyLevels.add(DifficultyLevel.EASY);
        difficultyLevels.add(DifficultyLevel.MEDIUM);
        difficultyLevels.add(DifficultyLevel.HARD);
        return difficultyLevels;
    }

    public static boolean isValidDifficulty(String difficultyName) {
        return getDifficultyLevels().indexOf(new DifficultyLevel(difficultyName)) >= 0;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DifficultyLevel that = (DifficultyLevel) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
