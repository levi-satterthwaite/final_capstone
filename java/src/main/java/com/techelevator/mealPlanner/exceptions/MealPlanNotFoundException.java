package com.techelevator.mealPlanner.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Meal plan not found.")
public class MealPlanNotFoundException extends MealPlanException {

    public MealPlanNotFoundException() {
        super("The meal plan you are searching for cannot be found.");
    }
}
