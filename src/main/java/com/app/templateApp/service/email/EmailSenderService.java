package com.app.templateApp.service.email;

import com.app.templateApp.entity.user.User;
import com.app.templateApp.entity.user.ConfirmationToken;

public interface EmailSenderService {

    void sendVerificationEmail(User user, ConfirmationToken confirmationToken);
}
