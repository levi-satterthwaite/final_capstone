package com.techelevator.recipes.controller;

import com.techelevator.recipes.dao.RecipeDAO;
import com.techelevator.recipes.model.Recipe;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
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
    @RequestMapping(path = "/recipes/{id}", method = RequestMethod.GET)
    public Recipe getById(@PathVariable(name = "id") Long recipeId) {
        return recipeDAO.getRecipeById(recipeId);
    }
    @RequestMapping(path = "/recipes", method = RequestMethod.POST)
    public Recipe addRecipe(@RequestBody Recipe recipe) {
        recipe.setDateCreated(LocalDate.now());
        return recipeDAO.addRecipe(recipe);
    }

}
