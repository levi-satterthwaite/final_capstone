package com.techelevator.mealPlanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Meal already exists.")
public class MealAlreadyExistsException extends MealException {

    public MealAlreadyExistsException() {
        super("The meal you are trying to add already exists.");
    }

    public MealAlreadyExistsException(String message) {
        super(message);
    }

    public MealAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MealAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public MealAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
