package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.engine.workflow.WorkflowComponentType;
import com.licenta.licenta.engine.workflow.WorkflowComponent;

import java.util.Map;

public class EndTask implements WorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.END_TASK;

    @Override
    public void execute(Map<String, Object> inputParameters) {

    }

    @Override
    public void setNext(WorkflowComponent next) {

    }
}
