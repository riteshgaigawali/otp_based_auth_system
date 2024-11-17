package com.assignment.otp_based_auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.assignment.otp_based_auth.entity.OtpLog;

import java.util.Optional;

@Repository
public interface OtpLogRepository extends JpaRepository<OtpLog, Long> {
    Optional<OtpLog> findByMobileNumberAndStatus(String mobileNumber, String status);
}