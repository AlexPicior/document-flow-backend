package com.licenta.licenta.engine.workflow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class WorkflowComponentDTO {
    private String label;

    private String type;

    private String description;

    private Map<String, WorkflowComponentPropertyDTO> properties;

}
