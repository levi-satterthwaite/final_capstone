package com.techelevator.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Ingredient is in use.")
public class IngredientIsInUseException extends IngredientException {

    public IngredientIsInUseException() {
        super("The ingredient you are trying to delete is in use.");
    }

    public IngredientIsInUseException(String message) {
        super(message);
    }

    public IngredientIsInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public IngredientIsInUseException(Throwable cause) {
        super(cause);
    }

    public IngredientIsInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
