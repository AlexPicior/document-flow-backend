package com.licenta.licenta.security.controller;

import com.licenta.licenta.security.dto.AuthenticationRequestDto;
import com.licenta.licenta.security.dto.AuthenticationResponseDto;
import com.licenta.licenta.security.dto.UserDTO;
import com.licenta.licenta.security.service.AuthenticationService;
import com.licenta.licenta.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.base-path}/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register/client")
    public ResponseEntity<AuthenticationResponseDto> registerClient(@RequestBody AuthenticationRequestDto registerRequest) {
        AuthenticationResponseDto responseDto = authenticationService.registerUser(registerRequest);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/register/admin")
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
