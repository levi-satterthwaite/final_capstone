package com.techelevator.mealPlanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid meal.")
public class InvalidMealException extends MealException {

    public InvalidMealException() {
        super("Invalid Meal");
    }

    public InvalidMealException(String message) {
        super(message);
    }

    public InvalidMealException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMealException(Throwable cause) {
        super(cause);
    }

    public InvalidMealException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
