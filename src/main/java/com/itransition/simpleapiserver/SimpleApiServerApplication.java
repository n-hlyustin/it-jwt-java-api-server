package com.itransition.simpleapiserver;

import com.itransition.simpleapiserver.configuration.MainProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MainProperties.class)
public class SimpleApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleApiServerApplication.class, args);
    }

}
