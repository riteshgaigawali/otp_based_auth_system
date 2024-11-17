package com.assignment.otp_based_auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.assignment.otp_based_auth.entity.User;
import com.assignment.otp_based_auth.service.UserService;
import com.assignment.otp_based_auth.utility.DeviceFingerprintFilter;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    DeviceFingerprintFilter deviceFingerprintFilter;

    // Register user
    @PostMapping
    public User registerUser(@RequestBody User user) {
        user.setDeviceFingerprint(deviceFingerprintFilter.getFingerprintString());
        return userService.registerUser(user); // Pass the entire user object
    }
}