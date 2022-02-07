package com.itransition.simpleapiserver;

import com.itransition.simpleapiserver.configuration.SecurityJwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SecurityJwtProperties.class)
public class SimpleApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleApiServerApplication.class, args);
    }

}
