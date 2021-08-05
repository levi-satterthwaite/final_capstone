package com.techelevator.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Recipe already exists.")
public class RecipeAlreadyExistsException extends RecipeException {

    public RecipeAlreadyExistsException() {
        super("The recipe you are trying to add already exists.");
    }

    public RecipeAlreadyExistsException(String message) {
        super(message);
    }

    public RecipeAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecipeAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public RecipeAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
