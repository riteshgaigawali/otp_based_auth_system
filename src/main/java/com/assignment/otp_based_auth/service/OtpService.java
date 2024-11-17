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

    public String generateOtp(String mobileNumber) {

        // Find the active OTP for the mobile number
        Optional<OtpLog> existingOtpLog = otpLogRepository.findByMobileNumberAndStatus(mobileNumber, "ACTIVE");

        if (!existingOtpLog.isPresent()) {
            String otp = String.valueOf(new Random().nextInt(999999));
            OtpLog otpLog = new OtpLog();
            otpLog.setMobileNumber(mobileNumber);
            otpLog.setOtp(otp);
            otpLog.setCreatedAt(LocalDateTime.now());
            otpLog.setExpiryTime(LocalDateTime.now().plusMinutes(5));
            otpLog.setStatus("ACTIVE");
            otpLogRepository.save(otpLog);
            return otp;
        } else {
            return "";
        }
    }

    public boolean validateOtp(String mobileNumber, String otp) {
        Optional<OtpLog> otpLog = otpLogRepository.findByMobileNumberAndStatus(mobileNumber, "ACTIVE");
        if (otpLog.isPresent() && otpLog.get().getOtp().equals(otp)
                && otpLog.get().getExpiryTime().isAfter(LocalDateTime.now())) {
            otpLog.get().setStatus("USED");
            otpLogRepository.save(otpLog.get());
            return true;
        }
        return false;
    }

    public String resendOtp(String mobileNumber) {
        // Find the active OTP for the mobile number
        Optional<OtpLog> existingOtpLog = otpLogRepository.findByMobileNumberAndStatus(mobileNumber, "ACTIVE");

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
