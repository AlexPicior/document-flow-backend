package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.engine.workflow.AbstractWorkflowComponent;
import com.licenta.licenta.engine.workflow.WorkflowComponentType;

import java.util.HashMap;
import java.util.Map;

public class StoreDocumentsTask extends AbstractWorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.STORE_DOCUMENTS_TASK;

    @Override
    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        try {
            System.out.println("Sent documents to storage.");
            Thread.sleep(1000);
            System.out.println("Documents successfully stored.");
        } catch (InterruptedException e){

        }

        return new HashMap<>();
    }
}
