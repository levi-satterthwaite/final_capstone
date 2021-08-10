package com.techelevator.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.FORBIDDEN, reason = "User Does Not Exist.")
public class UserDoesNotExistException extends Exception {

    public UserDoesNotExistException() {
        super("User does not exist.");
    }

    public UserDoesNotExistException(String message) {
        super(message);
    }

    public UserDoesNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDoesNotExistException(Throwable cause) {
        super(cause);
    }

    public UserDoesNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
