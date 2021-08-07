package com.techelevator.mealPlanner.model;

import com.techelevator.mealPlanner.exceptions.InvalidMealException;
import com.techelevator.recipes.exceptions.InvalidRecipeException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Meal {

    private Long mealId;
    private String name;
    private String category;
    private String imageFileName;
    private List<Recipe> recipeList;

    public Meal() {
        this.recipeList = new ArrayList<>();
    }

    public void validate() throws InvalidMealException {
        if(!MealCategory.isValidCategory(getCategory())) {
            throw new InvalidMealException();
        }
    }


    public void validateRecipe() throws InvalidRecipeException {
        try {
            for(Recipe recipe : recipeList) {
                recipe.validate();
            }
        } catch (RecipeException e) {
            throw new InvalidRecipeException("Invalid Recipe", e);
        }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
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
        Meal meal = (Meal) o;
        return Objects.equals(mealId, meal.mealId) && Objects.equals(name, meal.name) && Objects.equals(category, meal.category) && Objects.equals(recipeList, meal.recipeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mealId, name, category, recipeList);
    }
}
