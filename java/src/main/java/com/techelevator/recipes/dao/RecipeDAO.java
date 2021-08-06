package com.techelevator.recipes.dao;

import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeAlreadyExistsException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Ingredient;
import com.techelevator.recipes.model.Recipe;

import java.util.List;

public interface RecipeDAO {

    List<Recipe> getListOfRecipes();
    List<Recipe> getRecipesByName(String name);
    Recipe getRecipeById(Long recipeId) throws RecipeNotFoundException;
    Recipe addRecipe(Recipe recipe) throws NegativeValueException, RecipeException;
    Recipe addIngredientsToRecipe(Recipe recipe, List<Ingredient> ingredients) throws NegativeValueException, RecipeNotFoundException;
    void deleteRecipe (Recipe recipe) throws RecipeNotFoundException, RecipeException;
    void deleteIngredientsFromRecipe(Recipe recipe, List<Ingredient> ingredients) throws RecipeException;
}
