package com.assignment.otp_based_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection for APIs
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/register", "/api/auth/**").permitAll() // Allow
                                                                                      // these
                                                                                      // APIs
                                                                                      // without
                        // authentication
                        .anyRequest().authenticated() // Secure all other endpoints
                )
                .formLogin().disable() // Disable form login if it's not used
                .httpBasic().disable(); // Disable HTTP Basic Authentication if not required

        return http.build();
    }
}
