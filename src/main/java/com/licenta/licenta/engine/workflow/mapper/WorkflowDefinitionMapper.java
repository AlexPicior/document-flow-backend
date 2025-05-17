package com.licenta.licenta.engine.workflow.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.licenta.licenta.engine.workflow.dto.WorkflowDefinitionDTO;
import com.licenta.licenta.engine.workflow.model.WorkflowDefinition;
import com.licenta.licenta.engine.workflow.dto.WorkflowJSONDefinition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = { WorkflowDefinitionMapperHelper.class })
public interface WorkflowDefinitionMapper {
    WorkflowDefinitionMapper INSTANCE = Mappers.getMapper(WorkflowDefinitionMapper.class);
    ObjectMapper objectMapper = new ObjectMapper();

    @Mapping(source = "lastModifiedBy", target = "lastModifiedById", qualifiedByName = "lastModifiedByToLastModifiedById")
    @Mapping(source = "lastModifiedBy", target = "lastModifiedBy", qualifiedByName = "lastModifiedByToLastModifiedBy")
    @Mapping(source = "organisation", target = "organisationId", qualifiedByName = "organisationToOrganisationId")
    @Mapping(target = "jsonDefinition", source = "workflowDefinition.jsonDefinition", qualifiedByName = "workflowJSONDefinitionToJSON")
    WorkflowDefinitionDTO toDTO(WorkflowDefinition workflowDefinition);

    @Mapping(source = "lastModifiedById", target = "lastModifiedBy", qualifiedByName = "lastModifiedByIdToLastModifiedBy")
    @Mapping(source = "organisationId", target = "organisation", qualifiedByName = "organisationIdToOrganisation")
    @Mapping(source = "jsonDefinition", target = "jsonDefinition", qualifiedByName = "jsonToWorkflowJSONDefinition")
    WorkflowDefinition toEntity(WorkflowDefinitionDTO workflowDefinitionDTO);

    @Named("workflowJSONDefinitionToJSON")
    default String workflowJSONDefinitionToJSON(WorkflowJSONDefinition jsonDefinition) {
        try {
            return objectMapper.writeValueAsString(jsonDefinition);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting WorkflowJSONDefinition to JSON", e);
        }
    }

    @Named("jsonToWorkflowJSONDefinition")
    default WorkflowJSONDefinition jsonToWorkflowJSONDefinition(String jsonDefinition) {
        try {
            return objectMapper.readValue(jsonDefinition, WorkflowJSONDefinition.class);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to WorkflowJSONDefinition", e);
        } catch (Exception e) {
            int test = 1;
            throw new RuntimeException("Error converting JSON to WorkflowJSONDefinition", e);
        }
    }
}
