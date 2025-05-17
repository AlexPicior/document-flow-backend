package com.licenta.licenta.engine.workflow;

import com.licenta.licenta.engine.workflow.components.EndTask;
import com.licenta.licenta.engine.workflow.components.StartTask;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class WorkflowInstance {
    private StartTask startTask;
    private EndTask endTask;

    public void start(Map<String, Object> inputParameters) {
        startTask.execute(inputParameters);
    }

}
