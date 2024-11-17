/**
 * This is an utility class to generate a unique device fingerprint based on request attributes.
 * This fingerprint is used to identify and differentiate devices during authentication.
 */
package com.assignment.otp_based_auth.utility;

import org.springframework.util.DigestUtils;

import jakarta.servlet.http.HttpServletRequest;

public class DeviceFingerprintUtil {

    /**
     * Generates a unique device fingerprint by combining key request attributes.
     * The fingerprint is a hashed string created using the MD5 algorithm.
     * It takes the HTTP request from which to extract attributes and a unique
     * MD5-based fingerprint string.
     */
    public static String generateFingerprint(HttpServletRequest request) {
        // Retrieve the "User-Agent" header (browser and OS details).
        String userAgent = request.getHeader("User-Agent");
        // Retrieve the IP address of the client making the request.
        String ipAddress = request.getRemoteAddr();
        // Retrieve the "Accept-Language" header (preferred languages).
        String acceptLanguage = request.getHeader("Accept-Language");
        // Combine the attributes into a single string for fingerprinting.
        String fingerprintData = userAgent + ipAddress + acceptLanguage;
        // Hash the combined string using MD5 for a unique fingerprint.
        return DigestUtils.md5DigestAsHex(fingerprintData.getBytes());
    }
}