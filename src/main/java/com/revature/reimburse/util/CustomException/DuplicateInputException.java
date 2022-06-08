package com.revature.reimburse.util.CustomException;

public class DuplicateInputException extends RuntimeException {
    public DuplicateInputException(String type) {
        super(type+" already exists.");
    }
}
