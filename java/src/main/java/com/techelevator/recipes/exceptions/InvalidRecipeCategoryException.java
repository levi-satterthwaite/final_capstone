package com.techelevator.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid category.")
public class InvalidRecipeCategoryException extends RecipeException {

    public InvalidRecipeCategoryException() {
        super("Invalid Category Selection");
    }

    public InvalidRecipeCategoryException(String message) {
        super(message);
    }

    public InvalidRecipeCategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRecipeCategoryException(Throwable cause) {
        super(cause);
    }

    public InvalidRecipeCategoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
