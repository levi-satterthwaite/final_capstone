package com.techelevator.mealPlanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid meal plan.")
public class InvalidMealPlanException extends MealPlanException {

    public InvalidMealPlanException() {
        super("Invalid Meal Plan");
    }

    public InvalidMealPlanException(String message) {
        super(message);
    }

    public InvalidMealPlanException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMealPlanException(Throwable cause) {
        super(cause);
    }

    public InvalidMealPlanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
