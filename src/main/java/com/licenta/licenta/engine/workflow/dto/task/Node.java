package com.licenta.licenta.engine.workflow.dto.task;

import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import lombok.Data;

@Data
public class Node {
    private String id;
    private WorkflowComponentType type;
    private NodePosition position;
    private NodeData data;
}
