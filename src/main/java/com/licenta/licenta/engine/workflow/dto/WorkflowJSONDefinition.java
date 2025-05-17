package com.licenta.licenta.engine.workflow.dto;

import com.licenta.licenta.engine.workflow.dto.task.Edge;
import com.licenta.licenta.engine.workflow.dto.task.Node;
import lombok.Data;

import java.util.List;

@Data
public class WorkflowJSONDefinition {
    private List<Node> nodes;
    private List<Edge> edges;
}
