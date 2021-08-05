package com.techelevator.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Recipe not found.")
public class RecipeNotFoundException extends RecipeException {

    public RecipeNotFoundException() {
        super("The recipe you are searching for cannot be found.");
    }
}
