package com.techelevator.recipes.exceptions;

public class IngredientException extends  Exception {
    public IngredientException() {
        super();
    }

    public IngredientException(String message) {
        super(message);
    }

    public IngredientException(String message, Throwable cause) {
        super(message, cause);
    }

    public IngredientException(Throwable cause) {
        super(cause);
    }

    public IngredientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
