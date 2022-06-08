package com.revature.reimburse.util.CustomException;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String type) {
        super("Invalid "+type+" input.");
    }
}
