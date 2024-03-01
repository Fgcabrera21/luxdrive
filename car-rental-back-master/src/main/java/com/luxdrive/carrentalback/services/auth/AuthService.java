package com.luxdrive.carrentalback.services.auth;

import com.luxdrive.carrentalback.dto.RegisterRequest;
import com.luxdrive.carrentalback.dto.UserDto;

public interface AuthService {
    UserDto createCustomer(RegisterRequest registerRequest);
    boolean isAlreadyRegistered(String email);
}
