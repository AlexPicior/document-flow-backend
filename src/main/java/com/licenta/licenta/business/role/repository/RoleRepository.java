package com.licenta.licenta.business.role.repository;

import com.licenta.licenta.business.role.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByOrganisationId(Long organisationId);
}
