package com.licenta.licenta.engine.workflow.repository;

import com.licenta.licenta.engine.workflow.model.WorkflowComponentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkflowComponentRepository extends JpaRepository<WorkflowComponentModel, Long>  {
}
