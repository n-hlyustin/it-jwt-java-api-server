package com.itransition.simpleapiserver.mock;

import com.itransition.simpleapiserver.security.JwtHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@RequiredArgsConstructor
public class AuthRequestPostProcessor implements RequestPostProcessor {

    private final Long userId;
    private final JwtHelper jwtHelper;

    @SneakyThrows
    @Override
    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
        String token = jwtHelper.generateToken(userId);
        request.addHeader("Authorization", "Bearer " + token);
        return request;
    }

}