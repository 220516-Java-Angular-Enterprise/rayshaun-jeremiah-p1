package com.revature.reimburse.util.CustomException;

public class InvalidSQLException extends RuntimeException{
   public InvalidSQLException(String message){
        super(message);
    }
}
