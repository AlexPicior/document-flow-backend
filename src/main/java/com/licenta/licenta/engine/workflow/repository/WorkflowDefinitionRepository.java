package com.licenta.licenta.engine.workflow.repository;

import com.licenta.licenta.engine.workflow.model.WorkflowDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkflowDefinitionRepository extends JpaRepository<WorkflowDefinition, Long> {
    List<WorkflowDefinition> findAllByOrganisationId(Long organisationId);
}
