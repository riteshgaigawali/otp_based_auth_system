// This class includes functionality for generate OTP, resend OTP and validate the OTP.
package com.assignment.otp_based_auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.otp_based_auth.entity.OtpLog;
import com.assignment.otp_based_auth.repository.OtpLogRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {
    @Autowired
    private OtpLogRepository otpLogRepository;

    // This method generates a random OTP and return it to the caller method.
    public String generateOtp(String mobileNumber) {

        // Find the active OTP for the mobile number from database.
        Optional<OtpLog> existingOtpLog = otpLogRepository.findByMobileNumberAndStatus(mobileNumber, "ACTIVE");

        // If no Active OTP found then only generate a new OTP for mobile number else
        // return empty string.
        if (!existingOtpLog.isPresent()) {
            String otp = String.valueOf(new Random().nextInt(999999));
            OtpLog otpLog = new OtpLog();
            otpLog.setMobileNumber(mobileNumber);
            otpLog.setOtp(otp);
            otpLog.setCreatedAt(LocalDateTime.now());
            otpLog.setExpiryTime(LocalDateTime.now().plusMinutes(5));// OTP expires after 5 mins of it's generation.
            otpLog.setStatus("ACTIVE");
            otpLogRepository.save(otpLog);
            return otp;
        } else {
            return "";
        }
    }

    // This method is responsible for matching the opt sent by user and the opt in
    // database.
    public boolean validateOtp(String mobileNumber, String otp) {

        // Find the active OTP for the mobile number from database.
        Optional<OtpLog> otpLog = otpLogRepository.findByMobileNumberAndStatus(mobileNumber, "ACTIVE");
        // Check if the active otp from database and opt sent by user matches and is
        // expired or not. If
        // matched setStatus to USED and return true else return false.
        if (otpLog.isPresent() && otpLog.get().getOtp().equals(otp)
                && otpLog.get().getExpiryTime().isAfter(LocalDateTime.now())) {
            otpLog.get().setStatus("USED");
            otpLogRepository.save(otpLog.get());
            return true;
        }
        return false;
    }

    // If otp is expired this method will generate a new otp and send it to user.
    public String resendOtp(String mobileNumber) {

        // Find the active OTP for the mobile number from database.
        Optional<OtpLog> existingOtpLog = otpLogRepository.findByMobileNumberAndStatus(mobileNumber, "ACTIVE");
        // If already exsting otp found then only resend the otp and set previous otp
        // status to USED, else return empty
        // string.
        if (existingOtpLog.isPresent()) {
            // Mark the existing OTP as "USED"
            OtpLog otpLog = existingOtpLog.get();
            otpLog.setStatus("USED");
            otpLogRepository.save(otpLog);
            // Generate a new OTP
            String newOtp = generateOtp(mobileNumber);
            return newOtp;
        }

        return "";

    }

}
