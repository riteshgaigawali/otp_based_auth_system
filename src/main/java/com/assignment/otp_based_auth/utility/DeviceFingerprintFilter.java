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

    String fingerprint;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        fingerprint = DeviceFingerprintUtil.generateFingerprint(httpRequest);
        System.out.println("Device Fingerprint: " + fingerprint);
        chain.doFilter(request, response);
    }

    public String getFingerprintString() {
        return fingerprint;
    }

}