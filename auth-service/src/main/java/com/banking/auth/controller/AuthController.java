package com.banking.auth.controller;

import com.banking.auth.entity.UserEntity;
import com.banking.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserEntity user, @RequestParam String role) {
        return service.register(user, role);
    }

    @PostMapping("/login")
    public String initiateLogin(@RequestBody UserEntity credentials) {
        return service.initiateLogin(credentials.getUsername(), credentials.getPassword());
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String username, @RequestParam String otp) {
        return service.verifyOtpAndIssueTokens(username, otp);
    }

    @PostMapping("/refresh")
    public String refresh(@RequestParam String refreshToken) {
        return service.refreshToken(refreshToken);
    }
}