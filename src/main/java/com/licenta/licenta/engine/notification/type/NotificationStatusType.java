package com.licenta.licenta.engine.notification.type;

import lombok.Getter;

@Getter
public enum NotificationStatusType {
    WAITING("WAITING"), RESPONDED("RESPONDED"), APPROVED("APPROVED"), REJECTED("REJECTED");

    private final String label;

    NotificationStatusType(String label) {
        this.label = label;
    }

    public static NotificationStatusType fromString(String label) {
        for (NotificationStatusType notificationStatusType : NotificationStatusType.values()) {
            if (notificationStatusType.getLabel().equals(label)) {
                return notificationStatusType;
            }
        }
        throw new IllegalArgumentException("No enum constant with label: " + label);
    }
}
