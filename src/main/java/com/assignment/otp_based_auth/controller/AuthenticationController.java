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

    // End-point for login the user with mobile number and otp.
    @PostMapping("/login")
    public ResponseEntity<?> loginWithOtp(@RequestParam String mobileNumber, HttpServletRequest request) {

        // Get the user and check device_fingerprint
        User user = userService.getUserDetails(mobileNumber);
        String currentFingerprint = DeviceFingerprintUtil.generateFingerprint(request);
        if (!currentFingerprint.equals(user.getDeviceFingerprint())) {
            // Notify user about the new device via sms.
            twilioService.sendSms(mobileNumber, "Alert! - New device detected for your account.");
            user.setDeviceFingerprint(currentFingerprint);
        }

        // Call the generateOtp method from OtpService.
        String otp = otpService.generateOtp(mobileNumber);
        if (otp != "") {
            // Send OTP via Twilio
            twilioService.sendSms(mobileNumber, otp);
            return ResponseEntity.ok(Collections.singletonMap("message", "OTP sent to: " + mobileNumber));
        }
        return ResponseEntity
                .ok(Collections.singletonMap("message", "Otp already sent, if not received try resend-otp !"));
    }

    // End-point for resending the otp if it is expired.
    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam("mobileNumber") String mobileNumber) {

        // Call the resendOtp method from OtpService.
        String newOtp = otpService.resendOtp(mobileNumber);
        if (newOtp != "") {
            // Send OTP via Twilio
            twilioService.sendSms(mobileNumber, newOtp);
            return ResponseEntity.ok(Collections.singletonMap("message", "New OTP sent to: " + mobileNumber));
        }
        return ResponseEntity.ok(Collections.singletonMap("message", "There is no existing otp for: " + mobileNumber));
    }

    // End-point for validating the sent otp and otp inputed by user.
    @PostMapping("/validate-otp")
    public ResponseEntity<String> validateOtp(@RequestParam String mobileNumber, @RequestParam String otp) {
        if (otpService.validateOtp(mobileNumber, otp)) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
    }

    // Endpoint to get user details.
    @GetMapping("/user/{mobileNumber}")
    public User getUserDetails(@PathVariable String mobileNumber) {
        return userService.getUserDetails(mobileNumber);
    }
}