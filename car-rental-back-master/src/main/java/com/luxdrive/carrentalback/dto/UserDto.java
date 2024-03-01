package com.luxdrive.carrentalback.dto;

import com.luxdrive.carrentalback.enums.UserRole;

import lombok.Data;

@Data
public class UserDto {
    private Long id;


    private String name;
    private String email;
    private UserRole userRole;
}
