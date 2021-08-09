package com.techelevator.mealPlanner.controller;

import com.techelevator.dao.UserDAO;
import com.techelevator.mealPlanner.dao.MealDAO;
import com.techelevator.mealPlanner.exceptions.MealException;
import com.techelevator.mealPlanner.exceptions.MealNotFoundException;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.mealPlanner.model.MealCategory;
import com.techelevator.model.User;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Message;
import com.techelevator.recipes.model.Recipe;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@RestController
public class MealController {

    private MealDAO mealDAO;
    private UserDAO userDAO;

    public MealController(MealDAO mealDAO, UserDAO userDAO) {
        this.mealDAO = mealDAO;
        this.userDAO = userDAO;
    }

//    @RequestMapping(path = "/mealplans", method = RequestMethod.GET)
//    public List<Meal> getList() throws RecipeNotFoundException {
//        return mealDAO.getListOfMealPlans();
//    }

    @RequestMapping(path = "/meals", method = RequestMethod.GET)
    public List<Meal> getByName(@RequestParam(required = false, defaultValue = "") String name, Principal principal) throws
            RecipeNotFoundException {
        User user = userDAO.findByUsername(principal.getName());
        return mealDAO.getMealByName(name, user.getId());
    }

    @RequestMapping(path = "/meals/categories", method = RequestMethod.GET)
    public List<MealCategory> getCategories() {
        return MealCategory.getCategories();
    }

    @RequestMapping(path = "/meals/{id}", method = RequestMethod.GET)
    public Meal getById(@PathVariable(name = "id") Long mealId, Principal principal) throws MealNotFoundException,
            RecipeNotFoundException {
        User user = userDAO.findByUsername(principal.getName());
        return mealDAO.getMealById(mealId, user.getId());
    }

    @RequestMapping(path = "/meals", method = RequestMethod.POST)
    public Meal addMeals(@RequestBody Meal meal, Principal principal) throws MealException {
        User user = userDAO.findByUsername(principal.getName());
        return mealDAO.addMeal(meal, user.getId());
    }

    @RequestMapping(path = "/meals/{id}/recipes", method = RequestMethod.POST)
    public Meal addRecipes(@PathVariable(name = "id") Long mealId, @RequestBody List<Recipe> recipes, Principal principal) throws
            MealNotFoundException, RecipeNotFoundException {
        User user = userDAO.findByUsername(principal.getName());
        Meal meal = mealDAO.getMealById(mealId, user.getId());
        return mealDAO.addRecipesToMeal(meal, recipes, user.getId());
    }

    @RequestMapping(path = "/meals/{id}", method = RequestMethod.PUT)
    public Meal updateMeal(@PathVariable(name = "id") Long mealId, @RequestBody Meal meal, Principal principal) throws
            MealException, RecipeException, NegativeValueException {
        if(!mealId.equals(meal.getMealId())) {
            throw new MealException("Meal IDs do not match.");
        }
        User user = userDAO.findByUsername(principal.getName());
        mealDAO.getMealById(mealId, user.getId());
        return mealDAO.updateMeal(meal, user.getId());
    }

    @RequestMapping(path = "/meals/{id}", method = RequestMethod.DELETE)
    public Message deleteMeal(@PathVariable(name = "id") Long mealId, Principal principal) throws MealNotFoundException,
            RecipeException {
        User user = userDAO.findByUsername(principal.getName());
        mealDAO.deleteMeal(mealDAO.getMealById(mealId, user.getId()), user.getId());
        return new Message("The meal has been deleted.");
    }
}
