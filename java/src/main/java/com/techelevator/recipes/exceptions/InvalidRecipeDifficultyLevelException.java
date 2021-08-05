package com.techelevator.recipes.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid difficulty level.")
public class InvalidRecipeDifficultyLevelException extends RecipeException {

    public InvalidRecipeDifficultyLevelException() {
        super("Invalid Difficulty Level Selection");
    }

    public InvalidRecipeDifficultyLevelException(String message) {
        super(message);
    }

    public InvalidRecipeDifficultyLevelException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRecipeDifficultyLevelException(Throwable cause) {
        super(cause);
    }

    public InvalidRecipeDifficultyLevelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
