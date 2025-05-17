package com.licenta.licenta.business.role.mapper;

import com.licenta.licenta.business.organisation.service.OrganisationService;
import com.licenta.licenta.business.role.dto.RoleDTO;
import com.licenta.licenta.business.role.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final OrganisationService organisationService;

    public List<RoleDTO> allToDTO(List<Role> roles) {
        return roles.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Role toEntity(RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setLabel(roleDTO.getLabel());
        role.setOrganisation(organisationService.getOrganisationById(roleDTO.getOrganisationId()));
        return role;
    }

    public RoleDTO toDto(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setLabel(role.getLabel());
        roleDTO.setOrganisationId(role.getOrganisation().getId());
        return roleDTO;
    }
}
