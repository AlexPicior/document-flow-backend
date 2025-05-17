package com.licenta.licenta.engine.workflow.dto.task;

import lombok.Data;

@Data
public class Edge {
    private String id;
    private String source;
    private String sourceHandle;
    private String target;
}
