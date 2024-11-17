package com.assignment.otp_based_auth.utility;

import org.springframework.util.DigestUtils;

import jakarta.servlet.http.HttpServletRequest;

public class DeviceFingerprintUtil {

    public static String generateFingerprint(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();
        String acceptLanguage = request.getHeader("Accept-Language");
        String fingerprintData = userAgent + ipAddress + acceptLanguage;
        return DigestUtils.md5DigestAsHex(fingerprintData.getBytes());
    }
}