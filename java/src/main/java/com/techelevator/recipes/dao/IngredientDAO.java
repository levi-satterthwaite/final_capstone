package com.techelevator.recipes.dao;

import com.techelevator.recipes.exceptions.IngredientException;
import com.techelevator.recipes.exceptions.IngredientNotFoundException;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.model.Ingredient;

import java.util.List;

public interface IngredientDAO {

    Ingredient addIngredient(Ingredient ingredient) throws IngredientException;
    Ingredient getIngredientById(Long ingredientId) throws IngredientNotFoundException;
    List<Ingredient> getIngredientsByName(String name);

}
