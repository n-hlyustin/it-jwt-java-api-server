package com.itransition.simpleapiserver.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
class JwtProperties {
    private String secret;
}

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app")
public class MainProperties {
    private JwtProperties jwt;

    public String getJwtSecret() {
        return this.jwt.getSecret();
    }
}
