package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.engine.workflow.dto.task.TaskProperty;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import com.licenta.licenta.engine.workflow.WorkflowComponent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class StartTask implements WorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.START_TASK;
    private WorkflowComponent next;

    public static final String TRIGGERED_BY = "triggeredBy";
    public static final String EMPLOYEES = "employees";
    public static final String ROLES = "roles";
    public static final String SENDER_REFERENCE_NAME = "senderReferenceName";
    public static final String FORM_TYPE = "formType";
    public static final String FORM_REFERENCE_NAME = "formReferenceName";

    private String senderReferenceName;
    private String formReferenceName;

    // Trigger by options
    public static final String CLIENT = "client";
    public static final String EMPLOYEE = "employee";
    public static final String ROLE = "role";

    public StartTask(Map<String, TaskProperty> properties) {
        this.formReferenceName = (String) properties.get(FORM_REFERENCE_NAME).getValue();
        this.senderReferenceName = (String) properties.get(SENDER_REFERENCE_NAME).getValue();
    }

    @Override
    public void execute(Map<String, Object> inputParameters) {
        next.execute(inputParameters);
    }

    @Override
    public void setNext(WorkflowComponent next) {
        this.next = next;
    }
}
