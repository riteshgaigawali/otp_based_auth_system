package com.assignment.otp_based_auth.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private String accountSid = "AC928abb9a35cb79e5944f714d0052d189";

    private String authToken = "41b4b0f07141c0666b19a8d758a6af9f";

    private String fromPhoneNumber = "+17069404038";

    public void sendSms(String toPhoneNumber, String msg) {
        // Initialize Twilio with credentials
        Twilio.init(accountSid, authToken);

        // Send the SMS
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(toPhoneNumber), // To
                new com.twilio.type.PhoneNumber(fromPhoneNumber), // From
                msg // Message content
        ).create();

        System.out.println("SMS sent! SID: " + message.getSid());
    }
}