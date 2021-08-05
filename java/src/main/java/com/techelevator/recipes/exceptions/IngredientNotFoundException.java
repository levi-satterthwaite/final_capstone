package com.techelevator.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Ingredient not found.")
public class IngredientNotFoundException extends IngredientException {

    public IngredientNotFoundException() {
        super("The ingredient you are searching for cannot be found.");
    }


}
