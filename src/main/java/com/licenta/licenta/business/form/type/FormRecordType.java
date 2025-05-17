package com.licenta.licenta.business.form.type;

import lombok.Getter;


@Getter
public enum FormRecordType {
    STORE("store"), NOTIFICATION("notification");

    private final String label;

    FormRecordType(String label) {
        this.label = label;
    }

    public static FormRecordType fromString(String label) {
        for (FormRecordType formRecordType : FormRecordType.values()) {
            if (formRecordType.getLabel().equals(label)) {
                return formRecordType;
            }
        }
        throw new IllegalArgumentException("No enum constant with label: " + label);
    }
}
