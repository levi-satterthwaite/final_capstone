package com.techelevator.mealPlanner.controller;

import com.techelevator.mealPlanner.dao.MealPlanDAO;
import com.techelevator.mealPlanner.exceptions.MealPlanException;
import com.techelevator.mealPlanner.model.MealPlan;
import com.techelevator.recipes.model.Recipe;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class MealPlanController {

    private MealPlanDAO mealPlanDAO;

    public MealPlanController(MealPlanDAO mealPlanDAO) {
        this.mealPlanDAO = mealPlanDAO;
    }

    @RequestMapping(path = "/mealplans", method = RequestMethod.GET)
    public List<MealPlan> getList() {
        return mealPlanDAO.getListOfMealPlans();
    }

    @RequestMapping(path = "/mealplans", method = RequestMethod.GET)
    public List<MealPlan> getByName(@RequestParam String name) {
        return mealPlanDAO.getMealPlansByName(name);
    }

    @RequestMapping(path = "/mealplans/{id}", method = RequestMethod.GET)
    public MealPlan getById(@PathVariable(name = "id") Long mealId) {
        return mealPlanDAO.getMealPlanById(mealId);
    }

    @RequestMapping(path = "/mealplans", method = RequestMethod.POST)
    public MealPlan addMealPlans(@RequestBody MealPlan mealPlan) throws MealPlanException {
        return mealPlanDAO.addMealPlan(mealPlan);
    }

    @RequestMapping(path = "/mealplans/{id}/recipes", method = RequestMethod.POST)
    public MealPlan addRecipes(@PathVariable(name = "id") Long mealId, @RequestBody List<Recipe> recipes) {
        MealPlan mealPlan = mealPlanDAO.getMealPlanById(mealId);
        return mealPlanDAO.addRecipesToMealPlan(mealPlan, recipes);
    }



}