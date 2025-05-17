package com.licenta.licenta.engine.workflow.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class WorkflowComponentPropertyDTO {
    private Integer orderNumber;

    private String label;

    private String description;

    private String type;

    private String variableType;

    private Object value;

    private Map<String, WorkflowComponentPropertyDTO> next;

    private String parentKey;

    private List<WorkflowComponentPropertyOptionDTO> options;

    private String optionsType;

    private List<String> initialOptionTypes;


}
