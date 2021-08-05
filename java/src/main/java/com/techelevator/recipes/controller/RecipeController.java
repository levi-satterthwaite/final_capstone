package com.techelevator.recipes.controller;

import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.Ingredient;
import com.techelevator.recipes.model.Recipe;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.List;

@CrossOrigin
@RestController
public class RecipeController {

    private RecipeDAO recipeDAO;

    public RecipeController(RecipeDAO recipeDAO) {
        this.recipeDAO = recipeDAO;
    }

    @RequestMapping(path = "/recipes", method = RequestMethod.GET)
    public List<Recipe> getList() {
        return recipeDAO.getListOfRecipes();
    }

    @RequestMapping(path = "/recipes", method = RequestMethod.GET)
    public List<Recipe> getByName(@RequestParam String name) {
        return recipeDAO.getRecipesByName(name);
    }

    @RequestMapping(path = "/recipes/{id}", method = RequestMethod.GET)
    public Recipe getById(@PathVariable(name = "id") Long recipeId) throws RecipeNotFoundException {
        return recipeDAO.getRecipeById(recipeId);
    }

    @RequestMapping(path = "/recipes", method = RequestMethod.POST)
    public Recipe addRecipe(@RequestBody Recipe recipe) throws NegativeValueException {
        recipe.setDateCreated(LocalDate.now());
        return recipeDAO.addRecipe(recipe);
    }

    @RequestMapping(path = "/recipes/{id}/ingredients", method = RequestMethod.POST)
    public Recipe addIngredients(@PathVariable(name = "id") Long recipeId, @RequestBody List<Ingredient> ingredients) throws NegativeValueException, RecipeNotFoundException {
        Recipe recipe = recipeDAO.getRecipeById(recipeId);
        return recipeDAO.addIngredientsToRecipe(recipe, ingredients);
    }

}
