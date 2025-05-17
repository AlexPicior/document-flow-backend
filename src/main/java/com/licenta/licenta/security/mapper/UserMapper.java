package com.licenta.licenta.security.mapper;

import com.licenta.licenta.business.organisation.service.OrganisationService;
import com.licenta.licenta.business.role.mapper.RoleMapper;
import com.licenta.licenta.business.role.service.RoleService;
import com.licenta.licenta.security.dto.UserDTO;
import com.licenta.licenta.security.model.Authority;
import com.licenta.licenta.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final RoleMapper roleMapper;
    private final OrganisationService organisationService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    private final String defaultPassword = "test";

    public List<UserDTO> allToDTO(List<User> users) {
        return users.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAuthority(user.getAuthority() != null ? String.valueOf(user.getAuthority()) : null);
        userDTO.setRole(user.getRole() != null ? roleMapper.toDto(user.getRole()) : null);
        userDTO.setRoleId(user.getRole() != null ? user.getRole().getId() : null);
        userDTO.setPassword(user.getPassword());
        userDTO.setOrganisationId(user.getOrganisation() != null ? user.getOrganisation().getId() : null);
        return userDTO;
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword() != null ? userDTO.getPassword() : defaultPassword));
        user.setRole(userDTO.getRoleId() != null ? roleService.getRoleById(userDTO.getRoleId()) : null);
        user.setAuthority(userDTO.getAuthority() != null ? Authority.valueOf(userDTO.getAuthority()) : null);
        user.setOrganisation(organisationService.getOrganisationById(userDTO.getOrganisationId()));
        return user;
    }
}
