package com.app.templateApp.service.email;

import com.app.templateApp.entity.user.User;
import com.app.templateApp.entity.user.ConfirmationToken;

import java.io.IOException;

public interface EmailSenderService {

    void sendVerificationEmailHTML(User user, ConfirmationToken confirmationToken) throws IOException;
}
