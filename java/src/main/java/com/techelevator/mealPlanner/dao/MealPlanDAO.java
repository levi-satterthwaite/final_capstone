package com.techelevator.mealPlanner.dao;

import com.techelevator.mealPlanner.exceptions.MealPlanException;
import com.techelevator.mealPlanner.exceptions.MealPlanNotFoundException;
import com.techelevator.mealPlanner.model.MealPlan;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Recipe;

import java.util.List;

public interface MealPlanDAO {

    List<MealPlan> getListOfMealPlans() throws RecipeNotFoundException;
    List<MealPlan> getMealPlansByName(String name) throws RecipeNotFoundException;
    MealPlan getMealPlanById(Long mealId) throws MealPlanNotFoundException, RecipeNotFoundException;
    MealPlan addMealPlan(MealPlan mealPlan) throws MealPlanException;
    MealPlan addRecipesToMealPlan(MealPlan mealPlan, List<Recipe> recipes) throws MealPlanNotFoundException,
            RecipeNotFoundException;
    MealPlan updateMealPlan(MealPlan mealPlan) throws MealPlanException, RecipeException, NegativeValueException;
    void deleteMealPlan(MealPlan mealPlan) throws RecipeException;
    void deleteRecipesFromMealPlan(MealPlan mealPlan, List<Recipe> recipes) throws RecipeException;
}
