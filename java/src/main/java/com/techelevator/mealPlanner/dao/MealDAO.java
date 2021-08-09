package com.techelevator.mealPlanner.dao;

import com.techelevator.mealPlanner.exceptions.MealException;
import com.techelevator.mealPlanner.exceptions.MealNotFoundException;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Recipe;

import java.util.List;

public interface MealDAO {

    List<Meal> getListOfMeal() throws RecipeNotFoundException;
    List<Meal> getMealByName(String name, Long userId) throws RecipeNotFoundException;
    Meal getMealById(Long mealId, Long userId) throws MealNotFoundException, RecipeNotFoundException;
    Meal addMeal(Meal meal, Long userId) throws MealException;
    Meal addRecipesToMeal(Meal meal, List<Recipe> recipes, Long userId) throws MealNotFoundException,
            RecipeNotFoundException;
    Meal updateMeal(Meal meal, Long userId) throws MealException, RecipeException, NegativeValueException;
    void deleteMeal(Meal meal, Long userId) throws RecipeException;
    void deleteRecipesFromMeal(Meal meal, List<Recipe> recipes) throws RecipeException;
}
