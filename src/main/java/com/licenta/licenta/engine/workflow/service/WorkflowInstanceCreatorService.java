package com.licenta.licenta.engine.workflow.service;

import com.licenta.licenta.engine.workflow.WorkflowComponent;
import com.licenta.licenta.engine.workflow.WorkflowComponentType;
import com.licenta.licenta.engine.workflow.components.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowInstanceCreatorService {

    public WorkflowComponent getWorkflowComponentByType(WorkflowComponentType workflowComponentType) {
        return switch (workflowComponentType) {
            case START_TASK -> new StartTask();
            case SEND_EMAIL_TASK -> new SendEmailTask();
            case STORE_DOCUMENTS_TASK -> new StoreDocumentsTask();
            case WAIT_FOR_APPROVAL_TASK -> new WaitForApprovalTask();
            default -> new EndTask();
        };
    }
}
