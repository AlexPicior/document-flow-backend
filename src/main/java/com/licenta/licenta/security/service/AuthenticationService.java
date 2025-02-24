package com.licenta.licenta.security.service;

import com.licenta.licenta.security.dto.AuthenticationRequestDto;
import com.licenta.licenta.security.dto.AuthenticationResponseDto;
import com.licenta.licenta.security.model.Role;
import com.licenta.licenta.security.model.User;
import com.licenta.licenta.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthenticationResponseDto registerUser(AuthenticationRequestDto authenticationRequestDto) {
        return register(authenticationRequestDto, Role.USER);
    }

    public AuthenticationResponseDto registerAdmin(AuthenticationRequestDto authenticationRequestDto) {
        return register(authenticationRequestDto, Role.ADMIN);
    }

    private AuthenticationResponseDto register(AuthenticationRequestDto authenticationRequestDto, Role role) {
        if (userRepository.findByEmail(authenticationRequestDto.getEmail()).isPresent()) {
            throw new BadCredentialsException("Email already exists");
        }

        User user = new User();
        user.setEmail(authenticationRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(authenticationRequestDto.getPassword()));
        user.setRole(role);
        userRepository.save(user);

        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
        authenticationResponseDto.setToken(jwtService.generateToken(user));

        return authenticationResponseDto;
    }

    public AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto) {
        User user = userRepository.findByEmail(authenticationRequestDto.getEmail()).orElseThrow(() -> new BadCredentialsException("Wrong email"));
        if (!passwordEncoder.matches(authenticationRequestDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
        authenticationResponseDto.setToken(jwtService.generateToken(user));
        return authenticationResponseDto;
    }

}
