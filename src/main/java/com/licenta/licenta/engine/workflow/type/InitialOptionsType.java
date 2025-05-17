package com.licenta.licenta.engine.workflow.type;

import lombok.Getter;

@Getter
public enum InitialOptionsType {
    CONSTANT("constant"),
    ROLES("roles"),
    EMPLOYEES("employees"),
    FORMS("forms");

    private final String label;

    InitialOptionsType(String label) {
        this.label = label;
    }

    public static InitialOptionsType fromString(String label) {
        for (InitialOptionsType initialOptionsType : InitialOptionsType.values()) {
            if (initialOptionsType.getLabel().equals(label)) {
                return initialOptionsType;
            }
        }
        throw new IllegalArgumentException("No enum constant with label: " + label);
    }
}
