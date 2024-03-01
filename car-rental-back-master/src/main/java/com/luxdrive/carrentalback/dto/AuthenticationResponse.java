package com.luxdrive.carrentalback.dto;

import com.luxdrive.carrentalback.enums.UserRole;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String jwt;
    private UserRole userRole;
    private Long userId;
}
