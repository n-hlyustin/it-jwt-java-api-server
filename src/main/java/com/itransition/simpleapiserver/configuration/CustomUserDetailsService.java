package com.itransition.simpleapiserver.configuration;

import com.itransition.simpleapiserver.entities.User;
import com.itransition.simpleapiserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    public CustomUserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = userService.getUserById(id);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(username);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(user);
    }
}
