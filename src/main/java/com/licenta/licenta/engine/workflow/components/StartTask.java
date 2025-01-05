package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.engine.workflow.WorkflowComponentType;
import com.licenta.licenta.engine.workflow.WorkflowComponent;

import java.util.Map;

public class StartTask implements WorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.START_TASK;
    private WorkflowComponent next;

    @Override
    public void execute(Map<String, Object> inputParameters) {
        next.execute(inputParameters);
    }

    @Override
    public void setNext(WorkflowComponent next) {
        this.next = next;
    }
}
