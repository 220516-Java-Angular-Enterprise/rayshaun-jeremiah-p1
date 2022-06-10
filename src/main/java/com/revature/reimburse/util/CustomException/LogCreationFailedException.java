package com.revature.reimburse.util.CustomException;

public class LogCreationFailedException extends RuntimeException {
    public LogCreationFailedException(String message) {
        super(message);
    }
}
