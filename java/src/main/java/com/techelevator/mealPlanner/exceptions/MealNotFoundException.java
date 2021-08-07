package com.techelevator.mealPlanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Meal not found.")
public class MealNotFoundException extends MealException {

    public MealNotFoundException() {
        super("The meal you are searching for cannot be found.");
    }
}
