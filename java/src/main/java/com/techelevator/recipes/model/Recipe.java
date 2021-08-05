package com.techelevator.recipes.model;

import com.techelevator.recipes.exceptions.InvalidRecipeCategoryException;
import com.techelevator.recipes.exceptions.InvalidRecipeDifficultyLevelException;
import com.techelevator.recipes.exceptions.RecipeException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipe {

    private Long recipeId;
    private String name;
    private String category;
    private String difficultyLevel;
    private int prepTimeMin;
    private int cookTimeMin;
    private int servingSize;
    private String instructions;
    private LocalDate dateCreated;
    private String imageFileName;
    private List<Ingredient> ingredientList;

    public Recipe() {
        this.ingredientList = new ArrayList<>();
    }

    public void validate() throws RecipeException {
        if(!Category.isValidCategory(getCategory())) {
            throw new InvalidRecipeCategoryException();
        }
        if(!DifficultyLevel.isValidDifficulty(getDifficultyLevel())) {
            throw new InvalidRecipeDifficultyLevelException();
        }
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
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

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getPrepTimeMin() {
        return prepTimeMin;
    }

    public void setPrepTimeMin(int prepTimeMin) {
        this.prepTimeMin = prepTimeMin;
    }

    public int getCookTimeMin() {
        return cookTimeMin;
    }

    public void setCookTimeMin(int cookTimeMin) {
        this.cookTimeMin = cookTimeMin;
    }

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public List<Ingredient> getIngredientList() {
        return ingredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredientList.add(ingredient);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return prepTimeMin == recipe.prepTimeMin && cookTimeMin == recipe.cookTimeMin && servingSize == recipe.servingSize && Objects.equals(recipeId, recipe.recipeId) && Objects.equals(name, recipe.name) && Objects.equals(category, recipe.category) && Objects.equals(difficultyLevel, recipe.difficultyLevel) && Objects.equals(instructions, recipe.instructions) && Objects.equals(dateCreated, recipe.dateCreated) && Objects.equals(imageFileName, recipe.imageFileName) && Objects.equals(ingredientList, recipe.ingredientList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeId, name, category, difficultyLevel, prepTimeMin, cookTimeMin, servingSize, instructions, dateCreated, imageFileName, ingredientList);
    }
}
