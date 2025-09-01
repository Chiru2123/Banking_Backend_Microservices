package com.banking.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.skip-validation:false}")
    private boolean skipValidation;

    public String getSecret() {
        System.out.println("Loaded jwt.secret: " + secret);
        System.out.println("Loaded jwt.skip-validation: " + skipValidation);
        return secret;
    }
}