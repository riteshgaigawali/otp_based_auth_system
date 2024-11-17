package com.assignment.otp_based_auth.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.assignment.otp_based_auth.entity.User;
import com.assignment.otp_based_auth.service.OtpService;
import com.assignment.otp_based_auth.service.TwilioService;
import com.assignment.otp_based_auth.service.UserService;
import com.assignment.otp_based_auth.utility.DeviceFingerprintUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private OtpService otpService;
    @Autowired
    private UserService userService;
    @Autowired
    private TwilioService twilioService;

    @PostMapping("/login")
    public ResponseEntity<String> loginWithOtp(@RequestParam String mobileNumber, HttpServletRequest request) {

        // Get the user and check fingerprint
        User user = userService.getUserDetails(mobileNumber);
        String currentFingerprint = DeviceFingerprintUtil.generateFingerprint(request);

        if (!currentFingerprint.equals(user.getDeviceFingerprint())) {
            // Optionally log or notify about the new device
            twilioService.sendSms(mobileNumber, "Alert! - New device detected for your account.");
            user.setDeviceFingerprint(currentFingerprint);
        }

        // Generate OTP
        String otp = otpService.generateOtp(mobileNumber);

        // Send OTP via Twilio
        twilioService.sendSms(mobileNumber, "Your OTP is : " + otp);

        return ResponseEntity.ok("OTP sent to: " + mobileNumber); // Simulating the behavior.
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam("mobileNumber") String mobileNumber) {
        // Call the resendOtp method in OtpService
        String newOtp = otpService.resendOtp(mobileNumber);

        if (newOtp != "") {
            // Send OTP via Twilio
            twilioService.sendSms(mobileNumber, newOtp);

            return ResponseEntity.ok(Collections.singletonMap("message", "New OTP sent to: " + mobileNumber));
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "There is no existing otp for: " + mobileNumber));

    }

    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestParam String mobileNumber, @RequestParam String otp) {
        if (otpService.validateOtp(mobileNumber, otp)) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
    }

    // Endpoint to get user details
    @GetMapping("/user/{mobileNumber}")
    public User getUserDetails(@PathVariable String mobileNumber) {
        return userService.getUserDetails(mobileNumber);
    }
}