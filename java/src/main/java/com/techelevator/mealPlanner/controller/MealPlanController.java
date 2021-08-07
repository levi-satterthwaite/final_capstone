package com.techelevator.mealPlanner.controller;

import com.techelevator.mealPlanner.dao.MealPlanDAO;
import com.techelevator.mealPlanner.exceptions.*;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.mealPlanner.model.MealPlan;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Message;
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
    public List<MealPlan> getByName(@RequestParam(required = false, defaultValue = "") String name) throws
            MealPlanException, MealNotFoundException, RecipeNotFoundException {
        return mealPlanDAO.getMealPlanByName(name);
    }

    @RequestMapping(path = "/mealplans/{id}", method = RequestMethod.GET)
    public MealPlan getById(@PathVariable(name = "id") Long mealPlanId) throws MealPlanNotFoundException,
            RecipeNotFoundException, MealNotFoundException {
        return mealPlanDAO.getMealPlanById(mealPlanId);
    }

    @RequestMapping(path = "/mealplans", method = RequestMethod.POST)
    public MealPlan addMealPlans(@RequestBody MealPlan mealPlan) throws InvalidMealException, MealPlanException {
        return mealPlanDAO.addMealPlan(mealPlan);
    }

    @RequestMapping(path = "/mealplans/{id}/meals", method = RequestMethod.POST)
    public MealPlan addMeals(@PathVariable(name = "id") Long mealPlanId, @RequestBody List<Meal> meals) throws
            MealPlanNotFoundException, RecipeNotFoundException, MealNotFoundException {
        MealPlan mealPlan = mealPlanDAO.getMealPlanById(mealPlanId);
        return mealPlanDAO.addMealsToMealPlan(mealPlan, meals);
    }

    @RequestMapping(path = "/mealplans/{id}", method = RequestMethod.PUT)
    public MealPlan updateMealPlan(@PathVariable(name = "id") Long mealPlanId, @RequestBody MealPlan mealPlan) throws
            MealPlanException, MealNotFoundException, RecipeNotFoundException, InvalidMealException, NegativeValueException {
        if(!mealPlanId.equals(mealPlan.getMealPlanId())) {
            throw new MealPlanException("Meal Plan IDs do not match.");
        }
        mealPlanDAO.getMealPlanById(mealPlanId);
        return mealPlanDAO.updateMealPlan(mealPlan);
    }

    @RequestMapping(path = "/mealplans/{id}", method = RequestMethod.DELETE)
    public Message deleteMealPlan(@PathVariable(name = "id") Long mealPlanId) throws MealPlanNotFoundException,
            RecipeNotFoundException, MealNotFoundException {
        mealPlanDAO.deleteMealPlan(mealPlanDAO.getMealPlanById(mealPlanId));
        return new Message("The meal plan has been deleted.");
    }
}
