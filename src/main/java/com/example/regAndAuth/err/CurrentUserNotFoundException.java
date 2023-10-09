package com.example.regAndAuth.err;

public class CurrentUserNotFoundException extends Exception {

    public CurrentUserNotFoundException(String message) {
        super(message);
    }
}
