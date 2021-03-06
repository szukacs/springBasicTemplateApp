package com.app.templateApp.service.email;

import com.app.templateApp.entity.user.ConfirmationToken;
import com.app.templateApp.entity.user.User;
import com.app.templateApp.security.SecurityConstant;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

public class EmailTemplateService {
    private static String CONFIRMATION_MIDDLE_URL = "/users/confirm?token=";

    public static String VERIFICATION_EMAIL(User user, ConfirmationToken confirmationToken) throws IOException {
        String htmlString = readHtml("verificationEmailTemplate.html");
        String htmlReplacedData = htmlString.replace("[USERNAME]", user.getUserName()).replace("[URL_AND_TOKEN]", SecurityConstant.APP_BASE_URL + CONFIRMATION_MIDDLE_URL + confirmationToken.getConfirmationToken());
        return htmlReplacedData;
    }

    private static String readHtml(String filename) throws IOException {
        InputStream is = new ClassPathResource(filename).getInputStream();
        return IOUtils.toString(is);
    }
}
