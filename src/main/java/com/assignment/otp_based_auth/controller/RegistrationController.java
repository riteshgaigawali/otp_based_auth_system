package com.assignment.otp_based_auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.assignment.otp_based_auth.entity.User;
import com.assignment.otp_based_auth.service.UserService;
import com.assignment.otp_based_auth.utility.DeviceFingerprintFilter;

// End-point for user registration.
@RestController
@RequestMapping("/api/register")
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    DeviceFingerprintFilter deviceFingerprintFilter;

    @PostMapping
    public User registerUser(@RequestBody User user) {
        // Call getFingerprintString method from DeviceFingerprintFilter.
        user.setDeviceFingerprint(deviceFingerprintFilter.getFingerprintString());
        // Call the registerUser method from UserService.
        return userService.registerUser(user); // Pass the entire user object
    }
}