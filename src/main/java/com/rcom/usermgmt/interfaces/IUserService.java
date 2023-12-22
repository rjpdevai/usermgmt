package com.rcom.usermgmt.interfaces;

import com.rcom.usermgmt.models.User;
import com.rcom.usermgmt.registration.RegistrationRequest;
import com.rcom.usermgmt.registration.token.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getUsers();
    User registerUser(RegistrationRequest request);
    Optional<User> findByEmail(String email);

    void saveUserVerificationToken(User user, String verificationToken);

    String validateToken(String verificationToken);
}
