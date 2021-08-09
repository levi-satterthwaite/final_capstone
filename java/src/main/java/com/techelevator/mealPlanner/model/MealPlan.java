package com.techelevator.mealPlanner.model;

import com.techelevator.mealPlanner.exceptions.InvalidMealException;
import com.techelevator.mealPlanner.exceptions.MealException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MealPlan {

    private Long mealPlanId;
    private Long userId;
    private String name;
    private String description;
    private String imageFileName;
    private List<Meal> mealList;

    public MealPlan() {
        this.mealList = new ArrayList<>();
    }

    public void validateMeal() throws InvalidMealException {
        try {
            for(Meal meal : mealList) {
                meal.validate();
            }
        } catch (MealException e) {
            throw new InvalidMealException("Invalid Meal", e);
        }
    }

    public Long getMealPlanId() {
        return mealPlanId;
    }

    public void setMealPlanId(Long mealPlanId) {
        this.mealPlanId = mealPlanId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public List<Meal> getMealList() {
        return mealList;
    }

    public void setMealList(List<Meal> mealList) {
        this.mealList = mealList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealPlan mealPlan = (MealPlan) o;
        return Objects.equals(mealPlanId, mealPlan.mealPlanId) && Objects.equals(userId, mealPlan.userId) && Objects.equals(name, mealPlan.name) && Objects.equals(description, mealPlan.description) && Objects.equals(imageFileName, mealPlan.imageFileName) && Objects.equals(mealList, mealPlan.mealList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealPlanId, userId, name, description, imageFileName, mealList);
    }
}
