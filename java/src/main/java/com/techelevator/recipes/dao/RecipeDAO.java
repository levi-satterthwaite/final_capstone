package com.techelevator.recipes.dao;

import com.techelevator.model.User;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeAlreadyExistsException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Ingredient;
import com.techelevator.recipes.model.Recipe;

import java.util.List;

public interface RecipeDAO {

    List<Recipe> getListOfRecipes();
    List<Recipe> getRecipesByName(String name, Long userId);
    Recipe getRecipeById(Long recipeId, Long userId) throws RecipeNotFoundException;
    Recipe addRecipe(Recipe recipe) throws NegativeValueException, RecipeException;
    Recipe addIngredientsToRecipe(Recipe recipe, List<Ingredient> ingredients) throws NegativeValueException,
            RecipeNotFoundException;
    void deleteRecipe (Recipe recipe) throws RecipeException;
    void deleteIngredientsFromRecipe(Recipe recipe, List<Ingredient> ingredients) throws RecipeException;
    Recipe updateRecipe(Recipe recipe, Long userId) throws NegativeValueException, RecipeException;
}
