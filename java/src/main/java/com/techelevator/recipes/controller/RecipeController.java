package com.techelevator.recipes.controller;

import com.techelevator.dao.UserDAO;
import com.techelevator.model.User;
import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.exceptions.NegativeValueException;
import com.techelevator.recipes.exceptions.RecipeException;
import com.techelevator.recipes.exceptions.RecipeNotFoundException;
import com.techelevator.recipes.model.*;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;

import java.util.List;

@CrossOrigin
@RestController
public class RecipeController {

    private RecipeDAO recipeDAO;
    private UserDAO userDAO;

    public RecipeController(RecipeDAO recipeDAO, UserDAO userDAO) {
        this.recipeDAO = recipeDAO;
        this.userDAO = userDAO;
    }

    @RequestMapping(path = "/recipes", method = RequestMethod.GET)
    public List<Recipe> getByName(@RequestParam(required = false, defaultValue = "") String name, Principal principal) {
        User user = userDAO.findByUsername(principal.getName());
        return recipeDAO.getRecipesByName(name, user.getId());
    }

    @RequestMapping(path = "/recipes/categories", method = RequestMethod.GET)
    public List<RecipeCategory> getCategories() {
        return RecipeCategory.getCategories();
    }

    @RequestMapping(path = "/recipes/difficultylevels", method = RequestMethod.GET)
    public List<DifficultyLevel> getDifficultyLevels() {
        return DifficultyLevel.getDifficultyLevels();
    }

    @RequestMapping(path = "/recipes/{id}", method = RequestMethod.GET)
    public Recipe getById(@PathVariable(name = "id") Long recipeId, Principal principal) throws RecipeNotFoundException {
        User user = userDAO.findByUsername(principal.getName());
        return recipeDAO.getRecipeById(recipeId, user.getId());
    }

    @RequestMapping(path = "/recipes", method = RequestMethod.POST)
    public Recipe addRecipe(@RequestBody Recipe recipe, Principal principal) throws NegativeValueException, RecipeException {
        User user = userDAO.findByUsername(principal.getName());
        recipe.setUserId(user.getId());
        recipe.setDateCreated(LocalDate.now());
        return recipeDAO.addRecipe(recipe);
    }

    @RequestMapping(path = "/recipes/{id}/ingredients", method = RequestMethod.POST)
    public Recipe addIngredients(@PathVariable(name = "id") Long recipeId, @RequestBody List<Ingredient> ingredients, Principal principal)
            throws NegativeValueException, RecipeNotFoundException {
        User user = userDAO.findByUsername(principal.getName());
        Recipe recipe = recipeDAO.getRecipeById(recipeId, user.getId());
        return recipeDAO.addIngredientsToRecipe(recipe, ingredients);
    }

    @RequestMapping(path = "/recipes/{id}", method = RequestMethod.DELETE)
    public Message deleteRecipe(@PathVariable(name = "id") Long recipeId, Principal principal) throws RecipeException {
        User user = userDAO.findByUsername(principal.getName());
        recipeDAO.deleteRecipe(recipeDAO.getRecipeById(recipeId, user.getId()));
        return new Message("The recipe has been deleted.");
    }

    @RequestMapping(path = "/recipes/{id}", method = RequestMethod.PUT)
    public Recipe updateRecipe(@PathVariable(name = "id") Long recipeId, @RequestBody Recipe recipe, Principal principal) throws
            RecipeException, NegativeValueException {
        if(!recipeId.equals(recipe.getRecipeId())) {
            throw new RecipeException("Recipe IDs do not match.");
        }
        User user = userDAO.findByUsername(principal.getName());
        recipeDAO.getRecipeById(recipeId, user.getId());
        return recipeDAO.updateRecipe(recipe, user.getId());
    }


}
