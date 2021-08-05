package com.techelevator.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Ingredient already exists.")
public class IngredientAlreadyExistsException extends IngredientException {

    public IngredientAlreadyExistsException() {
        super("The ingredient you are trying to add already exists.");
    }

    public IngredientAlreadyExistsException(String message) {
        super(message);
    }

    public IngredientAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public IngredientAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public IngredientAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
