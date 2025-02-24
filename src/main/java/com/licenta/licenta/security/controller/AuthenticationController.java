package com.licenta.licenta.security.controller;

import com.licenta.licenta.security.dto.AuthenticationRequestDto;
import com.licenta.licenta.security.dto.AuthenticationResponseDto;
import com.licenta.licenta.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/registerUser")
    public ResponseEntity<AuthenticationResponseDto> registerUser(@RequestBody AuthenticationRequestDto registerRequest) {
        AuthenticationResponseDto responseDto = authenticationService.registerUser(registerRequest);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<AuthenticationResponseDto> registerAdmin(@RequestBody AuthenticationRequestDto registerRequest) {
        AuthenticationResponseDto responseDto = authenticationService.registerAdmin(registerRequest);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthenticationRequestDto loginRequest) {
        AuthenticationResponseDto res = authenticationService.login(loginRequest);
        return ResponseEntity.ok(res);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleIllegalArgumentException(BadCredentialsException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

}
