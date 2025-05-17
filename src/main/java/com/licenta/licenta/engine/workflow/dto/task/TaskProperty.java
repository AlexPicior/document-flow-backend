package com.licenta.licenta.engine.workflow.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.licenta.licenta.engine.workflow.type.TaskPropertyType;
import com.licenta.licenta.engine.workflow.type.VariableType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskProperty {
    private String label;
    private String description;
    private Integer orderNumber;
    private TaskPropertyType type;
    private Object value;
    private List<TaskPropertyOption> options;
    private VariableType optionsType;
    private VariableType variableType;
    private List<String> initialOptionTypes;
    private String parentKey;
    private Map<String, TaskProperty> next;
}
