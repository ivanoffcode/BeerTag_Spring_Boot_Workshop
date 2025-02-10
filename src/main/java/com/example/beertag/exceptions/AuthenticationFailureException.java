package com.example.beertag.exceptions;

public class AuthenticationFailureException extends RuntimeException {

    public AuthenticationFailureException (String message){
        super(message);
    }
}
