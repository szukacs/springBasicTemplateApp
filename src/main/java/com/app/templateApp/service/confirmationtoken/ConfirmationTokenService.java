package com.app.templateApp.service.confirmationtoken;

import com.app.templateApp.entity.user.ConfirmationToken;
import com.app.templateApp.exception.confirmationtoken.InvalidConfirmationTokenException;

public interface ConfirmationTokenService {

    ConfirmationToken saveConfirmationToken(ConfirmationToken token);

    void deleteConfirmationToken(Long id);

    ConfirmationToken findByToken(String confirmToken) throws InvalidConfirmationTokenException;
}
