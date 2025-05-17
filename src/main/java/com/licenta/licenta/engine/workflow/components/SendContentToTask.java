package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.engine.notification.TaskDispatcher;
import com.licenta.licenta.engine.notification.dto.NotificationDTO;
import com.licenta.licenta.engine.notification.type.NotificationStatusType;
import com.licenta.licenta.engine.notification.type.NotificationType;
import com.licenta.licenta.engine.workflow.AbstractSendWorkflowComponent;
import com.licenta.licenta.engine.workflow.dto.task.TaskProperty;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class SendContentToTask extends AbstractSendWorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.SEND_CONTENT_TO_TASK;

    public static final String TO = "to";
    public static final String CONTENT = "content";
    public static final String ADDITIONAL_MESSAGE = "additionalMessage";

    private Set<String> to;
    private List<String> content;
    private String additionalMessage;

    public SendContentToTask(Map<String, TaskProperty> properties) {
        this.to = getUserService().getUserIdsFromUserIdsAndRolesIds(
                getUserAndRolesIdsFromListOfIds((List<String>) properties.get(TO).getValue()));
        this.content = (List<String>) properties.get(CONTENT).getValue();
        this.additionalMessage = (String) properties.get(ADDITIONAL_MESSAGE).getValue();
        userVariableNames = getUserVariableNamesFromListOfIds((List<String>) properties.get(TO).getValue());
    }

    @Override
    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        TaskDispatcher taskDispatcher = getTaskDispatcher();
        to.addAll(getUserIdsFromInputParameters(inputParameters));
        to.forEach(userId -> content.forEach(contentVariableName -> {
            FormRecordDTO formRecordDTO = (FormRecordDTO) inputParameters.get(contentVariableName);
            createNotification(formRecordDTO, Long.valueOf(userId), additionalMessage, NotificationType.SIMPLE, null);
            taskDispatcher.sendNotificationToUser(userId, Map.of("type", NotificationType.SIMPLE.getLabel()));
        }));
//        System.out.println("Properties of " + TYPE + ": " + TO + ": " + String.join(",", to) + ", "
//                + CONTENT + ": " + String.join(",", content) + ", " + ADDITIONAL_MESSAGE + ": " + additionalMessage);
        return inputParameters;
    }

}
