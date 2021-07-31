package com.techelevator.recipes.dao;

import com.techelevator.recipes.model.Recipe;

import java.util.List;

public interface RecipeDAO {

    List<Recipe> getListOfRecipes();

    Recipe getRecipeById(Long recipeId);

    Recipe addRecipe(Recipe recipe);

}
