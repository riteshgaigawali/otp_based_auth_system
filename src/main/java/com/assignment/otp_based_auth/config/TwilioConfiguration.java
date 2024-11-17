package com.assignment.otp_based_auth.config;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class TwilioConfiguration {

    private String accountSid = "AC928abb9a35cb79e5944f714d0052d189";

    private String authToken = "41b4b0f07141c0666b19a8d758a6af9f";

    private String fromPhoneNumber = "+17069404038";

}
