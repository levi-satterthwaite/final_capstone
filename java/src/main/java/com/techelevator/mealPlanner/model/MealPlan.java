package com.techelevator.mealPlanner.model;

import com.techelevator.recipes.model.Recipe;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class MealPlan {

    private Long mealId;
    private String name;
    private String description;
    private List<Recipe> recipeList;

    public MealPlan() {

    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
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

    public List<Recipe> getRecipeList() {
        return recipeList;
    }

    public void setRecipeList(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealPlan mealPlan = (MealPlan) o;
        return Objects.equals(mealId, mealPlan.mealId) && Objects.equals(name, mealPlan.name) && Objects.equals(description, mealPlan.description) && Objects.equals(recipeList, mealPlan.recipeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealId, name, description, recipeList);
    }
}
