package com.licenta.licenta.engine.workflow.type;

import lombok.Getter;

@Getter
public enum ConditionType {
    IS("is"),
    IS_NOT("is_not"),
    CONTAINS("contains"),
    NOT_CONTAINS("not_contains"),
    EQUALS("="),
    NOT_EQUALS("!="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_EQUALS(">="),
    LESS_THAN_EQUALS("<="),;

    private final String label;

    ConditionType(String label) {
        this.label = label;
    }

    public static ConditionType fromString(String label) {
        for (ConditionType conditionType : ConditionType.values()) {
            if (conditionType.getLabel().equals(label)) {
                return conditionType;
            }
        }
        throw new IllegalArgumentException("No enum constant with label: " + label);
    }
}
