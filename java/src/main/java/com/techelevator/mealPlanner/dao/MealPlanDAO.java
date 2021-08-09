package com.techelevator.mealPlanner.dao;


import com.techelevator.mealPlanner.exceptions.*;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.mealPlanner.model.MealPlan;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;

import java.util.List;

public interface MealPlanDAO {

    List<MealPlan> getMealPlanByName(String name, Long userId) throws MealNotFoundException, RecipeNotFoundException, MealPlanException;
    MealPlan getMealPlanById(Long mealPlanId, Long userId) throws MealPlanNotFoundException, RecipeNotFoundException, MealNotFoundException;
    MealPlan addMealPlan(MealPlan mealPlan, Long userId) throws InvalidMealException, MealPlanException;
    MealPlan addMealsToMealPlan(MealPlan mealPlan, List<Meal> meals, Long userId) throws MealPlanNotFoundException, RecipeNotFoundException, MealNotFoundException;
    MealPlan updateMealPlan(MealPlan mealPlan, Long userId) throws MealPlanNotFoundException, InvalidMealException, RecipeNotFoundException, MealNotFoundException, NegativeValueException;
    void deleteMealPlan(MealPlan mealPlan, Long userId);
    void deleteMealsFromMealPlan(MealPlan mealPlan, List<Meal> meals);
}
