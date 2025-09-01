package com.banking.transaction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        System.out.println("Configuring Account Service SecurityWebFilterChain - disabling CSRF and permitting all");
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable) // Explicitly disable CSRF
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().permitAll() // Permit all for testing
            )
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .logout(ServerHttpSecurity.LogoutSpec::disable)
            .exceptionHandling(exchanges -> exchanges.authenticationEntryPoint((exchange, ex) -> {
                System.out.println("Authentication failed for path: " + exchange.getRequest().getPath().value() + " - " + ex.getMessage());
                return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
            }));

        return http.build();
    }
}