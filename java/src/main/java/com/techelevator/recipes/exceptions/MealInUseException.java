package com.techelevator.recipes.exceptions;

import com.techelevator.mealPlanner.exceptions.MealException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Meal is in use.")
public class MealInUseException extends MealException {

    public MealInUseException() {
        super("The meal you are trying to delete is in use.");
    }

    public MealInUseException(String message) {
        super(message);
    }

    public MealInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MealInUseException(Throwable cause) {
        super(cause);
    }

    public MealInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
