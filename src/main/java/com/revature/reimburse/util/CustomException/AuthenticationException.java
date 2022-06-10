package com.revature.reimburse.util.CustomException;

public class AuthenticationException extends RuntimeException{
  public  AuthenticationException(){
        super();
    }

    public AuthenticationException(String message){
        super(message);
    }
}
