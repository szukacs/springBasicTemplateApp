package com.app.templateApp.exception.user;

public class EmailExistException extends UserManipulationException {
    public EmailExistException(String message) {
        super(message);
    }
}
