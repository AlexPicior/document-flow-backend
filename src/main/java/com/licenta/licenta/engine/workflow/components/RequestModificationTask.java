package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.business.form.model.FormRecord;
import com.licenta.licenta.engine.notification.TaskDispatcher;
import com.licenta.licenta.engine.notification.type.NotificationStatusType;
import com.licenta.licenta.engine.notification.type.NotificationType;
import com.licenta.licenta.engine.workflow.AbstractSendWorkflowComponent;
import com.licenta.licenta.engine.workflow.AbstractWorkflowComponent;
import com.licenta.licenta.engine.workflow.dto.ReplyDTO;
import com.licenta.licenta.engine.workflow.dto.task.TaskProperty;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@NoArgsConstructor
public class RequestModificationTask extends AbstractSendWorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.REQUEST_MODIFICATION_TASK;

    public static final String FROM = "from";
    public static final String CONTENT = "content";
    public static final String MODIFIED_CONTENT = "modifiedContent";
    public static final String ADDITIONAL_MESSAGE = "additionalMessage";

    private String from;
    private String content;
    private String modifiedContent;
    private String additionalMessage;

    public RequestModificationTask(Map<String, TaskProperty> properties) {
        Set<String> userIds = getUserService().getUserIdsFromUserIdsAndRolesIds(
                getUserAndRolesIdsFromListOfIds(List.of((String) properties.get(FROM).getValue())));
        this.from = userIds.isEmpty() ? null : userIds.stream().findFirst().orElseThrow();
        this.content = (String) properties.get(CONTENT).getValue();
        this.modifiedContent = (String) properties.get(MODIFIED_CONTENT).getValue();
        this.additionalMessage = (String) properties.get(ADDITIONAL_MESSAGE).getValue();
        userVariableNames = getUserVariableNamesFromListOfIds(List.of((String) properties.get(FROM).getValue()));
    }

    @Override
    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        TaskDispatcher taskDispatcher = getTaskDispatcher();
        Map<String, Object> outputParameters = new HashMap<>(inputParameters);
        try {
            FormRecordDTO formRecordDTO = (FormRecordDTO) inputParameters.get(content);
            String receiverId = verifyAndGetFrom(inputParameters);

            String correlationId = UUID.randomUUID().toString();
            Long notificationId = createNotification(formRecordDTO, Long.valueOf(receiverId), additionalMessage, NotificationType.REQUEST, correlationId);
            ReplyDTO reply = taskDispatcher.sendAndWaitForReply(receiverId, Map.of("type", NotificationType.REQUEST.getLabel()), correlationId);
            updateNotificationAfterReply(notificationId, reply.getReplyForm());

            outputParameters.put(modifiedContent, reply.getReplyForm());
        } catch (InterruptedException e) {
            throw new RuntimeException(e); // TODO handle exception here better
        }
//        System.out.println("Properties of " + TYPE + ": " + FROM + ": " + from + ", "
//                + CONTENT + ": " + content + ", " + MODIFIED_CONTENT + ": " + modifiedContent);
        return outputParameters;
    }

    private String verifyAndGetFrom(Map<String, Object> inputParameters) {
        if (from == null) {
            return getUserIdsFromInputParameters(inputParameters).getFirst();
        }
        return from;
    }

    private Map<String, Object> getMessage(Map<String, Object> inputParameters) {
        Map<String, Object> message = new HashMap<>();
        message.put(CONTENT, inputParameters.get(content));
        message.put("workflowComponentType", TYPE);
        message.put(ADDITIONAL_MESSAGE, additionalMessage);
        return message;
    }
}
