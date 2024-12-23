// This class contains the methods that supports the user registration and fetching the user details from database.
package com.assignment.otp_based_auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.otp_based_auth.entity.User;
import com.assignment.otp_based_auth.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // This method is responsible for creating new user entry to User table in
    // database.
    public User registerUser(User user) {
        User u = user;
        u.setName(user.getName());
        u.setMobileNumber(user.getMobileNumber());
        u.setAddress(user.getAddress());
        u.setDob(user.getDob());
        u.setDeviceFingerprint(user.getDeviceFingerprint());

        // Save user to the database
        return userRepository.save(u);
    }

    // Method to get user details by mobile number from the database.
    public User getUserDetails(String mobileNumber) {
        Optional<User> user = userRepository.findByMobileNumber(mobileNumber);
        return user.orElse(null); // Return the user or null if not found
    }

}