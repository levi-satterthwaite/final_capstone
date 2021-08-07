package com.techelevator.mealPlanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class MealException extends Exception {

    public MealException() {
        super();
    }

    public MealException(String message) {
        super(message);
    }

    public MealException(String message, Throwable cause) {
        super(message, cause);
    }

    public MealException(Throwable cause) {
        super(cause);
    }

    public MealException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
