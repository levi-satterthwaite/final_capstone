package com.techelevator.recipes.dao;

import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.model.Ingredient;
import com.techelevator.recipes.model.Recipe;

import java.util.List;

public interface RecipeDAO {

    List<Recipe> getListOfRecipes();
    List<Recipe> getRecipesByName(String name);
    Recipe getRecipeById(Long recipeId);
    Recipe addRecipe(Recipe recipe) throws NegativeValueException;
    Recipe addIngredientsToRecipe(Recipe recipe, List<Ingredient> ingredients) throws NegativeValueException;

}
