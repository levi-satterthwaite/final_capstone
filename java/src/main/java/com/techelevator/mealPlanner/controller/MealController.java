package com.techelevator.mealPlanner.controller;

import com.techelevator.mealPlanner.dao.MealDAO;
import com.techelevator.mealPlanner.exceptions.MealException;
import com.techelevator.mealPlanner.exceptions.MealNotFoundException;
import com.techelevator.mealPlanner.model.Meal;
import com.techelevator.mealPlanner.model.MealCategory;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Message;
import com.techelevator.recipes.model.Recipe;
import com.techelevator.recipes.model.RecipeCategory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class MealController {

    private MealDAO mealDAO;

    public MealController(MealDAO mealDAO) {
        this.mealDAO = mealDAO;
    }

//    @RequestMapping(path = "/mealplans", method = RequestMethod.GET)
//    public List<Meal> getList() throws RecipeNotFoundException {
//        return mealDAO.getListOfMealPlans();
//    }

    @RequestMapping(path = "/meals", method = RequestMethod.GET)
    public List<Meal> getByName(@RequestParam(required = false, defaultValue = "") String name) throws
            RecipeNotFoundException {
        return mealDAO.getMealByName(name);
    }

    @RequestMapping(path = "/meals/categories", method = RequestMethod.GET)
    public List<MealCategory> getCategories() {
        return MealCategory.getCategories();
    }

    @RequestMapping(path = "/meals/{id}", method = RequestMethod.GET)
    public Meal getById(@PathVariable(name = "id") Long mealId) throws MealNotFoundException,
            RecipeNotFoundException {
        return mealDAO.getMealById(mealId);
    }

    @RequestMapping(path = "/meals", method = RequestMethod.POST)
    public Meal addMeals(@RequestBody Meal meal) throws MealException {
        return mealDAO.addMeal(meal);
    }

    @RequestMapping(path = "/meals/{id}/recipes", method = RequestMethod.POST)
    public Meal addRecipes(@PathVariable(name = "id") Long mealId, @RequestBody List<Recipe> recipes) throws
            MealNotFoundException, RecipeNotFoundException {
        Meal meal = mealDAO.getMealById(mealId);
        return mealDAO.addRecipesToMeal(meal, recipes);
    }

    @RequestMapping(path = "/meals/{id}", method = RequestMethod.PUT)
    public Meal updateMeal(@PathVariable(name = "id") Long mealId, @RequestBody Meal meal) throws
            MealException, RecipeException, NegativeValueException {
        if(!mealId.equals(meal.getMealId())) {
            throw new MealException("Meal IDs do not match.");
        }
        mealDAO.getMealById(mealId);
        return mealDAO.updateMeal(meal);
    }

    @RequestMapping(path = "/meals/{id}", method = RequestMethod.DELETE)
    public Message deleteMeal(@PathVariable(name = "id") Long mealId) throws MealNotFoundException, RecipeException {
        mealDAO.deleteMeal(mealDAO.getMealById(mealId));
        return new Message("The meal has been deleted.");
    }
}
