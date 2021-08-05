package com.techelevator.mealPlanner.dao;

import com.techelevator.mealPlanner.exceptions.MealPlanException;
import com.techelevator.mealPlanner.model.MealPlan;
import com.techelevator.recipes.model.Recipe;

import java.util.List;

public interface MealPlanDAO {

    List<MealPlan> getListOfMealPlans();
    List<MealPlan> getMealPlansByName(String name);
    MealPlan getMealPlanById(Long mealId);
    MealPlan addMealPlan(MealPlan mealPlan) throws MealPlanException;
    MealPlan addRecipesToMealPlan(MealPlan mealPlan, List<Recipe> recipes);

}
