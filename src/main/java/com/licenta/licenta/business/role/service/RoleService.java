package com.licenta.licenta.business.role.service;

import com.licenta.licenta.business.role.dto.RoleDTO;
import com.licenta.licenta.business.role.mapper.RoleMapper;
import com.licenta.licenta.business.role.model.Role;
import com.licenta.licenta.business.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow();
    }

    public List<RoleDTO> getAllRolesByOrganisationId(Long organisationId) {
        return roleMapper.allToDTO(roleRepository.findAllByOrganisationId(organisationId));
    }

    public RoleDTO saveRole(RoleDTO roleDTO) {
        return roleMapper.toDto(roleRepository.save(roleMapper.toEntity(roleDTO)));
    }

    public void deleteRoleById(Long roleId) {
        roleRepository.deleteById(roleId);
    }
}
