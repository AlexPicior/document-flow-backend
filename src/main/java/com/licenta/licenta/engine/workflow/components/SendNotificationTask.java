package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.engine.notification.TaskDispatcher;
import com.licenta.licenta.engine.notification.type.NotificationType;
import com.licenta.licenta.engine.workflow.AbstractSendWorkflowComponent;
import com.licenta.licenta.engine.workflow.AbstractWorkflowComponent;
import com.licenta.licenta.engine.workflow.dto.task.TaskProperty;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class SendNotificationTask extends AbstractSendWorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.SEND_NOTIFICATION_TASK;

    public static final String TO = "to";
    public static final String MESSAGE = "message";

    private Set<String> to;
    private String message;

    public SendNotificationTask(Map<String, TaskProperty> properties) {
        this.to = getUserService().getUserIdsFromUserIdsAndRolesIds(
                getUserAndRolesIdsFromListOfIds((List<String>) properties.get(TO).getValue()));
        this.message = (String) properties.get(MESSAGE).getValue();
        userVariableNames = getUserVariableNamesFromListOfIds((List<String>) properties.get(TO).getValue());
    }

    @Override
    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        TaskDispatcher taskDispatcher = getTaskDispatcher();
        to.addAll(getUserIdsFromInputParameters(inputParameters));
        to.forEach(userId -> {
            createNotification(null, Long.valueOf(userId), message, NotificationType.SIMPLE, null);
            taskDispatcher.sendNotificationToUser(userId, Map.of("type", NotificationType.SIMPLE.getLabel()));
        });
//        System.out.println("Properties of " + TYPE + ": " + TO + ": " + String.join(",", to) + ", "
//                + MESSAGE + ": " + message);
        return inputParameters;
    }

}
