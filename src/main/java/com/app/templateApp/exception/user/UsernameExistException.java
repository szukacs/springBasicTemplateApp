package com.app.templateApp.exception.user;

public class UsernameExistException extends UserManipulationException {
    public UsernameExistException(String message) {
        super(message);
    }
}
