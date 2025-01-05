package com.licenta.licenta.engine.workflow;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractWorkflowComponent implements WorkflowComponent {
    private WorkflowComponent next;

    @Override
    public void execute(Map<String, Object> inputParameters) {
        Map<String, Object> outputParameters = executeInternal(inputParameters);
        next.execute(outputParameters);
    }

    @Override
    public void setNext(WorkflowComponent next) {
        this.next = next;
    }

    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        return new HashMap<>();
    }
}
