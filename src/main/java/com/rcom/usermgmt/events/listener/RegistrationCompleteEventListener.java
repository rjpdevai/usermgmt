package com.rcom.usermgmt.events.listener;

import com.rcom.usermgmt.events.RegistrationCompleteEvent;
import com.rcom.usermgmt.models.User;
import com.rcom.usermgmt.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;
    private final JavaMailSender mailSender;
    private User user;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        user = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveUserVerificationToken(user, verificationToken);
        String url = event.getApplicationUrl() + "/register/verifyEmail?token="+verificationToken;
//        try {
//            sendVerificationEmail(url);
//        } catch (MessagingException | UnsupportedEncodingException e) {
//            throw new RuntimeException(e);
//        }
        log.info("Click the link to verify: "+url);
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Verify your email";
        String senderName = "Gam Registration";
        String mailContent = "<p> Hi, "+user.getFirstName()+", </p>"+
                "<p>Please, click this link or copy paste in browser to finish the registration of your account.</p>"+
                "<a href=\""+url+"\">Verify your email to activate your account</a>"+
                "<p>Thank you </br> Spring Boot App</p>";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("rit.niku@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
