package com.app.templateApp.exception.user;

public class EmailNotFoundException extends UserManipulationException {
    public EmailNotFoundException(String message) {
        super(message);
    }
}
