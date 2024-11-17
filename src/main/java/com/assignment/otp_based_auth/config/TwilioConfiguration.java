//Configuration class for third-party SMS service Twilio. 
package com.assignment.otp_based_auth.config;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class TwilioConfiguration {

    private String accountSid = "";

    private String authToken = "";

    private String fromPhoneNumber = "";

}
