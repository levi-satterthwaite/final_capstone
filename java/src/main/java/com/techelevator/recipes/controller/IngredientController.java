package com.techelevator.recipes.controller;

import com.techelevator.recipes.dao.IngredientDAO;
import com.techelevator.recipes.exceptions.IngredientException;
import com.techelevator.recipes.exceptions.IngredientNotFoundException;
import com.techelevator.recipes.model.Ingredient;
import com.techelevator.recipes.model.Message;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class IngredientController {

    private IngredientDAO ingredientDAO;

    public IngredientController(IngredientDAO ingredientDAO) {
        this.ingredientDAO = ingredientDAO;
    }

    @RequestMapping(path = "/ingredients", method = RequestMethod.POST)
    public Ingredient add(@RequestBody Ingredient ingredient) throws IngredientException {
        return ingredientDAO.addIngredient(ingredient);
    }

    @RequestMapping(path = "/ingredient/{id}", method = RequestMethod.GET)
    public Ingredient getById(@PathVariable(name = "id") Long ingredientId) throws IngredientException {
        return ingredientDAO.getIngredientById(ingredientId);
    }

    @RequestMapping(path = "/ingredients", method = RequestMethod.GET)
    public List<Ingredient> getByName(@RequestParam(required = false, defaultValue = "") String name) {
        return ingredientDAO.getIngredientsByName(name);
    }

//    @RequestMapping(path = "/ingredient/{id}", method = RequestMethod.DELETE)
//    public Message delete(@PathVariable(name = "id") Long ingredientId) throws IngredientException {
//        ingredientDAO.deleteIngredient(ingredientDAO.getIngredientById(ingredientId));
//        return new Message("The ingredient has been deleted.");
//    }

}
