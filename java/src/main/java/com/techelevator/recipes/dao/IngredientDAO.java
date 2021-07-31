package com.techelevator.recipes.dao;

import com.techelevator.recipes.model.Ingredient;

import java.util.List;

public interface IngredientDAO {

    Ingredient addIngredient(Ingredient ingredient);
    Ingredient getIngredientById(Long ingredientId);
    Ingredient getIngredientByName(String name);
    List<Ingredient> getIngredientsByRecipeId(Long recipeId);

}
