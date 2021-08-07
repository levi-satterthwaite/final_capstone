package com.techelevator.mealPlanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Meal plan already exists.")
public class MealPlanAlreadyExistsException extends MealPlanException {
    public MealPlanAlreadyExistsException() {
        super("The meal plan you are trying to add already exists.");
    }

    public MealPlanAlreadyExistsException(String message) {
        super(message);
    }

    public MealPlanAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MealPlanAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public MealPlanAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
