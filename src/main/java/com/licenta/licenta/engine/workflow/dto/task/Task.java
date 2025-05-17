package com.licenta.licenta.engine.workflow.dto.task;

import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import lombok.Data;

import java.util.Map;

@Data
public class Task {
    private WorkflowComponentType type;
    private String label;
    private String description;
    private Map<String, TaskProperty> properties;
}
