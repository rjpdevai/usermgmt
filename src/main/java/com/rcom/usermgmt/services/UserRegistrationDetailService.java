package com.rcom.usermgmt.services;

import com.rcom.usermgmt.repository.UserRepository;
import com.rcom.usermgmt.security.UserRegistrationDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationDetailService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(UserRegistrationDetail::new)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
