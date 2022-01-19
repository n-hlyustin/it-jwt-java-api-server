package com.itransition.simpleapiserver.controllers;

import com.itransition.simpleapiserver.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
public class MainController {

    @GetMapping(path = "/hello")
    public String hello(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return String.format("Hello, %s %s", user.getFirstName(), user.getLastName());
    }
}
