package com.licenta.licenta.security.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String email;
    private String password;
    private String organisationId;
}
