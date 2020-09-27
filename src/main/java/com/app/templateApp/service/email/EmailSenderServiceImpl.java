package com.app.templateApp.service.email;

import com.app.templateApp.entity.user.User;
import com.app.templateApp.security.SecurityConstant;
import com.app.templateApp.entity.user.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailSenderService")
public class EmailSenderServiceImpl implements EmailSenderService {
    private JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendVerificationEmail(User user, ConfirmationToken confirmationToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setFrom(System.getenv("EMAIL_USERNAME"));
        mailMessage.setText("Hello " + user.getUserName() + " , \n \nTo confirm your account, please click here : "
                + SecurityConstant.APP_BASE_URL +"/users/confirm?token=" + confirmationToken.getConfirmationToken() + " \n \nThe Zupper team!");
        sendEmail(mailMessage);
    }

    @Async
    private void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }
}
