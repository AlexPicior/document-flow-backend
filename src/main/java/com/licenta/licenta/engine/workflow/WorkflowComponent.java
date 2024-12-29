package com.licenta.licenta.engine.workflow;

import java.util.Map;

public interface WorkflowComponent {
    void execute(Map<String, Object> inputParameters);

    void setNext(WorkflowComponent next);
}

