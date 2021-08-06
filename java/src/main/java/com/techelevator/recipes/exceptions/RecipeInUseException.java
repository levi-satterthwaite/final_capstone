package com.techelevator.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Recipe is in use.")
public class RecipeInUseException extends RecipeException {

    public RecipeInUseException() {
        super("The recipe you are trying to delete is in use.");
    }

    public RecipeInUseException(String message) {
        super(message);
    }

    public RecipeInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecipeInUseException(Throwable cause) {
        super(cause);
    }

    public RecipeInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
