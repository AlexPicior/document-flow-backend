package com.licenta.licenta.engine.workflow.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum TaskPropertyType {
    @JsonProperty("single_text")
    SINGLE_TEXT("single_text"),
    @JsonProperty("multi_text")
    MULTI_TEXT("multi_text"),
    @JsonProperty("text_area")
    TEXT_AREA("text_area"),
    @JsonProperty("single_select")
    SINGLE_SELECT("single_select"),
    @JsonProperty("multi_select")
    MULTI_SELECT("multi_select"),
    @JsonProperty("number")
    NUMBER("number"),
    @JsonProperty("boolean")
    BOOLEAN("boolean"),
    @JsonProperty("variable")
    VARIABLE("variable"),
    @JsonProperty("condition")
    CONDITION("condition");

    private final String label;

    TaskPropertyType(String label) {
        this.label = label;
    }

    public static TaskPropertyType fromString(String label) {
        for (TaskPropertyType taskPropertyType : TaskPropertyType.values()) {
            if (taskPropertyType.getLabel().equals(label)) {
                return taskPropertyType;
            }
        }
        throw new IllegalArgumentException("No enum constant with label: " + label);
    }
}
