package com.licenta.licenta.security.dto;

import com.licenta.licenta.business.role.dto.RoleDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String authority;
    private Long roleId;
    private RoleDTO role;
    private Long organisationId;
}
