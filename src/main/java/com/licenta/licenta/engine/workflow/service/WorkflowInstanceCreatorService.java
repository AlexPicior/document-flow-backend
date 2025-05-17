package com.licenta.licenta.engine.workflow.service;

import com.licenta.licenta.engine.workflow.WorkflowComponent;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import com.licenta.licenta.engine.workflow.components.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowInstanceCreatorService {

    public WorkflowComponent getWorkflowComponentByType(WorkflowComponentType workflowComponentType) {
        return switch (workflowComponentType) {
            default -> new EndTask();
        };
    }
}
