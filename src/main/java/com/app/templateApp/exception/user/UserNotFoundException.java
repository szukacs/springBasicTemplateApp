package com.app.templateApp.exception.user;

public class UserNotFoundException extends UserManipulationException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
