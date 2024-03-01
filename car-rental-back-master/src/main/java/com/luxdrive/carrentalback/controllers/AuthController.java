package com.luxdrive.carrentalback.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luxdrive.carrentalback.dto.AuthenticationRequest;
import com.luxdrive.carrentalback.dto.AuthenticationResponse;
import com.luxdrive.carrentalback.dto.RegisterRequest;
import com.luxdrive.carrentalback.dto.UserDto;
import com.luxdrive.carrentalback.entities.User;
import com.luxdrive.carrentalback.repositories.UserRepository;
import com.luxdrive.carrentalback.services.auth.AuthService;
import com.luxdrive.carrentalback.services.jwt.UserService;
import com.luxdrive.carrentalback.utils.JWTUtil;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody RegisterRequest registerRequest) {
        logger.warn("I'm in the register");
        if (authService.isAlreadyRegistered(registerRequest.getEmail())) {
            return new ResponseEntity<>("Email already used !", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto createdCustomerDto = authService.createCustomer(registerRequest);

        if (createdCustomerDto == null) {
            return new ResponseEntity<>(
                    "Customer not created, error happened!", HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(createdCustomerDto, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws
            BadCredentialsException,
            DisabledException,
            UsernameNotFoundException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            logger.warn("i'm in the login catch");
            throw new BadCredentialsException("Incorrect email or password");
        }

        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;
    }

}
