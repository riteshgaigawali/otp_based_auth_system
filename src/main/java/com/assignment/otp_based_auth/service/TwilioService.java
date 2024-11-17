// This class is responsible for sending the sms to the provided mobile numbers.
package com.assignment.otp_based_auth.service;

import com.assignment.otp_based_auth.config.TwilioConfiguration;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    // Get Twilio credentials.
    @Autowired
    TwilioConfiguration twilioConfiguration;

    public void sendSms(String toPhoneNumber, String msg) {
        // Initialize Twilio with credentials
        Twilio.init(twilioConfiguration.getAccountSid(), twilioConfiguration.getAuthToken());

        // Send the SMS
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(toPhoneNumber), // To
                new com.twilio.type.PhoneNumber(twilioConfiguration.getFromPhoneNumber()), // From
                msg // Message content
        ).create();

        System.out.println("SMS sent! SID: " + message.getSid());
    }
}