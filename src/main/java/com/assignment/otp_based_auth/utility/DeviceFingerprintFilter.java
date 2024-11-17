/*
The purpose of this class is to generate a unique fingerprint for the device making the HTTP request. This fingerprint is used to track and differentiate between devices during user interactions, enhancing security.
 */
package com.assignment.otp_based_auth.utility;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class DeviceFingerprintFilter implements Filter {

    // Stores the generated fingerprint for the current request
    String fingerprint;

    /**
     * Intercepts each HTTP request, generates a device fingerprint, and logs it.
     * Then passes the request and response objects along the filter chain.
     *
     * request the incoming HTTP request
     * response the outgoing HTTP response
     * chain the filter chain for processing subsequent filters or the target
     * resource
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Cast the generic ServletRequest to HttpServletRequest for HTTP-specific
        // handling.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // Generate a unique fingerprint for the current request using utility class.
        fingerprint = DeviceFingerprintUtil.generateFingerprint(httpRequest);
        // Proceed to the next filter or the target resource in the chain.
        chain.doFilter(request, response);
    }

    // Retrieves the generated device fingerprint for the current request.
    public String getFingerprintString() {
        // Returns the device fingerprint.
        return fingerprint;
    }

}