package com.licenta.licenta.engine.workflow.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum VariableType {
    @JsonProperty("person")
    PERSON("person"),
    @JsonProperty("form")
    FORM("form");

    private final String label;

    VariableType(String label) {
        this.label = label;
    }

    public static VariableType fromString(String label) {
        for (VariableType variableType : VariableType.values()) {
            if (variableType.getLabel().equals(label)) {
                return variableType;
            }
        }
        throw new IllegalArgumentException("No enum constant with label: " + label);
    }

}
