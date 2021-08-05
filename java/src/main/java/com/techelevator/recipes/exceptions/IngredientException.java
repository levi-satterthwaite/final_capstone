package com.techelevator.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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
