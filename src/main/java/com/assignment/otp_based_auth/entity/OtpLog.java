package com.assignment.otp_based_auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "otp_logs")
@Data
public class OtpLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mobileNumber;
    private String otp;
    private LocalDateTime createdAt;
    private LocalDateTime expiryTime;
    private String status;
}
