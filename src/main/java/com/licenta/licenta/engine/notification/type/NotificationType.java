package com.licenta.licenta.engine.notification.type;

import lombok.Getter;

@Getter
public enum NotificationType {
    SIMPLE("simple"), REQUEST("request"), APPROVAL("approval");

    private final String label;

    NotificationType(String label) {
        this.label = label;
    }

    public static NotificationType fromString(String label) {
        for (NotificationType notificationType : NotificationType.values()) {
            if (notificationType.getLabel().equals(label)) {
                return notificationType;
            }
        }
        throw new IllegalArgumentException("No enum constant with label: " + label);
    }
}
