package com.banking.notification.controller;

import com.banking.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    @Autowired
    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping("/sms")
    public void sendSms(@RequestParam String phone, @RequestParam String message) {
        service.sendSms(phone, message);
    }

    @PostMapping("/otp")
    public String sendOtp(@RequestParam String phone, @RequestParam String message) {
        return service.sendOtp(phone, message);
    }

    @PostMapping("/email")
    public void sendEmail(@RequestParam String email, @RequestParam String message) {
        service.sendEmail(email, message);
    }
}