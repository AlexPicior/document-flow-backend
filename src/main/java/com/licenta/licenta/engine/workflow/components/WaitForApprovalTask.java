package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.engine.workflow.AbstractWorkflowComponent;
import com.licenta.licenta.engine.workflow.WorkflowComponentType;

import java.util.HashMap;
import java.util.Map;

public class WaitForApprovalTask extends AbstractWorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.WAIT_FOR_APPROVAL_TASK;

    @Override
    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        try {
            System.out.println("Sent request for approval.");
            Thread.sleep(2000);
            System.out.println("Received approval.");
        } catch (InterruptedException e){

        }

        return new HashMap<>();
    }
}
