package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.engine.workflow.AbstractWorkflowComponent;
import com.licenta.licenta.engine.workflow.WorkflowComponentType;

import java.util.HashMap;
import java.util.Map;

public class SendEmailTask extends AbstractWorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.SEND_EMAIL_TASK;

    @Override
    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        System.out.println("Sent email.");
        return new HashMap<>();
    }
}
