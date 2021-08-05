package com.techelevator.mealPlanner.exceptions;

public class MealPlanException extends Exception {

    public MealPlanException() {
        super();
    }

    public MealPlanException(String message) {
        super(message);
    }

    public MealPlanException(String message, Throwable cause) {
        super(message, cause);
    }

    public MealPlanException(Throwable cause) {
        super(cause);
    }

    public MealPlanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
