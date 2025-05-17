package com.licenta.licenta.engine.workflow.dto;

import com.licenta.licenta.security.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WorkflowDefinitionDTO {
    private Long id;

    private String lastModifiedById;

    private String organisationId;

    private String label;

    private String description;

    private LocalDateTime lastModifiedAt;

    private UserDTO lastModifiedBy;

    private String jsonDefinition;
}
