package com.rcom.usermgmt.registration;

import com.rcom.usermgmt.events.RegistrationCompleteEvent;
import com.rcom.usermgmt.models.User;
import com.rcom.usermgmt.registration.token.VerificationToken;
import com.rcom.usermgmt.repository.VerificationTokenRepository;
import com.rcom.usermgmt.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;

    private final ApplicationEventPublisher applicationEventPublisher;
    private final VerificationTokenRepository verificationTokenRepository;
    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        User user = userService.registerUser(registrationRequest);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Success!, Please check your email for verification!";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if(verificationToken.getUser().isEnabled()){
            return "This account is already verified, please login";
        }
        return userService.validateToken(token);

    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
