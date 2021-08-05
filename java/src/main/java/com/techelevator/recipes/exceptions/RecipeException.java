package com.techelevator.recipes.exceptions;

public class RecipeException extends Exception {

    public RecipeException() {
        super();
    }

    public RecipeException(String message) {
        super(message);
    }

    public RecipeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecipeException(Throwable cause) {
        super(cause);
    }

    public RecipeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
