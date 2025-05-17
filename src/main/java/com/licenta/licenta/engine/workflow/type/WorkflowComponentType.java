package com.licenta.licenta.engine.workflow.type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum WorkflowComponentType {
    @JsonProperty("startNode")
    START_TASK("startNode"),
    @JsonProperty("endNode")
    END_TASK("endNode"),
    @JsonProperty("sendContentToTask")
    SEND_CONTENT_TO_TASK("sendContentToTask"),
    @JsonProperty("approvalTask")
    APPROVAL_TASK("approvalTask"),
    @JsonProperty("requestModificationTask")
    REQUEST_MODIFICATION_TASK("requestModificationTask"),
    @JsonProperty("completeFormTask")
    COMPLETE_FORM_TASK("completeFormTask"),
    @JsonProperty("sendEmailTask")
    SEND_EMAIL_TASK("sendEmailTask"),
    @JsonProperty("storeTask")
    STORE_TASK("storeTask"),
    @JsonProperty("sendNotificationTask")
    SEND_NOTIFICATION_TASK("sendNotificationTask"),
    @JsonProperty("conditionalTask")
    CONDITIONAL_TASK("conditionalTask"),;

    private final String label;

    WorkflowComponentType(String label) {
        this.label = label;
    }

    public static WorkflowComponentType fromString(String label) {
        for (WorkflowComponentType workflowComponentType : WorkflowComponentType.values()) {
            if (workflowComponentType.getLabel().equals(label)) {
                return workflowComponentType;
            }
        }
        throw new IllegalArgumentException("No enum constant with label: " + label);
    }
}
