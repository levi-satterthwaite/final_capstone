package com.techelevator.mealPlanner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MealCategory {

    public static final MealCategory BREAKFAST = new MealCategory("Breakfast");
    public static final MealCategory LUNCH = new MealCategory("Lunch");
    public static final MealCategory DINNER = new MealCategory("Dinner");
    public static final MealCategory SNACK = new MealCategory("Snack");

    private String name;

    public MealCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static List<MealCategory> getCategories() {
        List<MealCategory> categories = new ArrayList<MealCategory>();
        categories.add(MealCategory.BREAKFAST);
        categories.add(MealCategory.LUNCH);
        categories.add(MealCategory.DINNER);
        categories.add(MealCategory.SNACK);
        return categories;
    }

    public static boolean isValidCategory(String categoryName) {
        return getCategories().indexOf(new MealCategory(categoryName)) >= 0;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealCategory that = (MealCategory) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
