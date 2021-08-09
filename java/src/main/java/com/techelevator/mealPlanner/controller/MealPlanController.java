package com.techelevator.mealPlanner.controller;

import com.techelevator.dao.UserDAO;
import com.techelevator.mealPlanner.dao.MealPlanDAO;
import com.techelevator.mealPlanner.exceptions.*;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.mealPlanner.model.MealPlan;
import com.techelevator.model.User;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Message;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
public class MealPlanController {

    private MealPlanDAO mealPlanDAO;
    private UserDAO userDAO;

    public MealPlanController(MealPlanDAO mealPlanDAO, UserDAO userDAO) {
        this.mealPlanDAO = mealPlanDAO;
        this.userDAO = userDAO;
    }

    @RequestMapping(path = "/mealplans", method = RequestMethod.GET)
    public List<MealPlan> getByName(@RequestParam(required = false, defaultValue = "") String name, Principal principal) throws
            MealPlanException, MealNotFoundException, RecipeNotFoundException {
        User user = userDAO.findByUsername(principal.getName());
        return mealPlanDAO.getMealPlanByName(name, user.getId());
    }

    @RequestMapping(path = "/mealplans/{id}", method = RequestMethod.GET)
    public MealPlan getById(@PathVariable(name = "id") Long mealPlanId, Principal principal) throws MealPlanNotFoundException,
            RecipeNotFoundException, MealNotFoundException {
        User user = userDAO.findByUsername(principal.getName());
        return mealPlanDAO.getMealPlanById(mealPlanId, user.getId());
    }

    @RequestMapping(path = "/mealplans", method = RequestMethod.POST)
    public MealPlan addMealPlans(@RequestBody MealPlan mealPlan, Principal principal) throws InvalidMealException, MealPlanException {
        User user = userDAO.findByUsername(principal.getName());
        return mealPlanDAO.addMealPlan(mealPlan, user.getId());
    }

    @RequestMapping(path = "/mealplans/{id}/meals", method = RequestMethod.POST)
    public MealPlan addMeals(@PathVariable(name = "id") Long mealPlanId, @RequestBody List<Meal> meals, Principal principal) throws
            MealPlanNotFoundException, RecipeNotFoundException, MealNotFoundException {
        User user = userDAO.findByUsername(principal.getName());
        MealPlan mealPlan = mealPlanDAO.getMealPlanById(mealPlanId, user.getId());
        return mealPlanDAO.addMealsToMealPlan(mealPlan, meals, user.getId());
    }

    @RequestMapping(path = "/mealplans/{id}", method = RequestMethod.PUT)
    public MealPlan updateMealPlan(@PathVariable(name = "id") Long mealPlanId, @RequestBody MealPlan mealPlan, Principal principal) throws
            MealPlanException, MealNotFoundException, RecipeNotFoundException, InvalidMealException, NegativeValueException {
        if(!mealPlanId.equals(mealPlan.getMealPlanId())) {
            throw new MealPlanException("Meal Plan IDs do not match.");
        }
        User user = userDAO.findByUsername(principal.getName());
        mealPlanDAO.getMealPlanById(mealPlanId, user.getId());
        return mealPlanDAO.updateMealPlan(mealPlan, user.getId());
    }

    @RequestMapping(path = "/mealplans/{id}", method = RequestMethod.DELETE)
    public Message deleteMealPlan(@PathVariable(name = "id") Long mealPlanId, Principal principal) throws MealPlanNotFoundException,
            RecipeNotFoundException, MealNotFoundException {
        User user = userDAO.findByUsername(principal.getName());
        mealPlanDAO.deleteMealPlan(mealPlanDAO.getMealPlanById(mealPlanId, user.getId()), user.getId());
        return new Message("The meal plan has been deleted.");
    }
}
