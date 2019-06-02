package com.example.javaApiTutorial.exceptions;

public class InvalidRequestBodyException extends RuntimeException{

    public InvalidRequestBodyException(String message) {
        super(message);
    }

}