package com.licenta.licenta.engine.workflow.mapper;

import com.licenta.licenta.business.organisation.model.Organisation;
import com.licenta.licenta.business.organisation.service.OrganisationService;
import com.licenta.licenta.security.dto.UserDTO;
import com.licenta.licenta.security.mapper.UserMapper;
import com.licenta.licenta.security.model.User;
import com.licenta.licenta.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkflowDefinitionMapperHelper {
    private final OrganisationService organisationService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Named("lastModifiedByToLastModifiedById")
    public Long lastModifiedByToLastModifiedById(User lastModifiedBy) {
        return lastModifiedBy.getId();
    }

    @Named("lastModifiedByToLastModifiedBy")
    public UserDTO lastModifiedByToLastModifiedBy(User lastModifiedBy) {
        return userMapper.toDTO(lastModifiedBy);
    }

    @Named("lastModifiedByIdToLastModifiedBy")
    public User lastModifiedByIdToLastModifiedBy(Long lastModifiedById) {
        return userService.getUserById(lastModifiedById);
    }

    @Named("organisationToOrganisationId")
    public Long organisationToOrganisationId(Organisation organisation  ) {
        return organisation.getId();
    }

    @Named("organisationIdToOrganisation")
    public Organisation organisationIdToOrganisation(Long organisationId) {
        return organisationService.getOrganisationById(organisationId);
    }

}
