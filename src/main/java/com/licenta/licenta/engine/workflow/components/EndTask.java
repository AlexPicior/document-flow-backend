package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import com.licenta.licenta.engine.workflow.WorkflowComponent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class EndTask implements WorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.END_TASK;

    private String test;

    @Override
    public void execute(Map<String, Object> inputParameters) {

    }

    @Override
    public void setNext(WorkflowComponent next) {

    }
}
