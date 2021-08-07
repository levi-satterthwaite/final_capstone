package com.techelevator.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid recipe.")
public class InvalidRecipeException extends RecipeException {

    public InvalidRecipeException() {
        super("Invalid Recipe");
    }

    public InvalidRecipeException(String message) {
        super(message);
    }

    public InvalidRecipeException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRecipeException(Throwable cause) {
        super(cause);
    }

    public InvalidRecipeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
