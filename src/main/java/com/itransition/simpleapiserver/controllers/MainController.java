package com.itransition.simpleapiserver.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class MainController {

    @GetMapping(path = "/hello")
    @ResponseStatus(value = HttpStatus.OK)
    public String hello(Principal principal) {
        return String.format("Hello, %s", principal.getName());
    }
}
