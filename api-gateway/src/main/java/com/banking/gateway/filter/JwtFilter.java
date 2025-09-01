package com.banking.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.List;

@Component
public class JwtFilter implements GlobalFilter, Ordered {

    private final String secret;
    private final boolean skipValidation;

    @Autowired
    public JwtFilter(com.banking.gateway.config.JwtConfig jwtConfig, @Value("${jwt.skip-validation:false}") boolean skipValidation) {
        this.secret = jwtConfig.getSecret();
        this.skipValidation = skipValidation;
        System.out.println("JwtFilter initialized with skipValidation: " + skipValidation);
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        System.out.println("JwtFilter processing request for path: " + path + " with skipValidation: " + skipValidation + " and method: " + exchange.getRequest().getMethod());

        if (path.startsWith("/auth/")) {
            System.out.println("Skipping JWT validation for path: " + path);
            return chain.filter(exchange);
        }

        if (skipValidation) {
            System.out.println("Skipping JWT validation for testing (skipValidation=true)");
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    "testuser", null, List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
            SecurityContextHolder.getContext().setAuthentication(auth);
            System.out.println("SecurityContext set with role: ROLE_CUSTOMER");
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        System.out.println("Authorization header value: " + token);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            System.out.println("Extracted token: " + token);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSigningKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
                System.out.println("Token claims: " + claims);
                String role = claims.get("role", String.class);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        claims.getSubject(), null, List.of(new SimpleGrantedAuthority(role)));
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("SecurityContext set with role: " + role);
                return chain.filter(exchange);
            } catch (Exception e) {
                System.out.println("JWT validation failed for path: " + path + " - Error: " + e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        System.out.println("No valid token for path: " + path);
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1; // Higher priority than default filters
    }
}