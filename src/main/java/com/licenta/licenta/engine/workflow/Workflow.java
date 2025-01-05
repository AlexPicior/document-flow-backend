package com.licenta.licenta.engine.workflow;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class Workflow {
    private WorkflowComponent startTask;
    private WorkflowComponent endTask;

    public void start(Map<String, Object> inputParameters) {
        startTask.execute(inputParameters);
    }

}
