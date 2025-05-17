package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.engine.notification.type.NotificationStatusType;
import com.licenta.licenta.engine.notification.type.NotificationType;
import com.licenta.licenta.engine.workflow.AbstractSendWorkflowComponent;
import com.licenta.licenta.engine.workflow.AbstractWorkflowComponent;
import com.licenta.licenta.engine.workflow.WorkflowComponent;
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
public class ApprovalTask extends AbstractSendWorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.APPROVAL_TASK;

    public static final String CONTENT_TO_APPROVE = "contentToApprove";
    public static final String BY = "by";
    public static final String ADDITIONAL_MESSAGE = "additionalMessage";

    // Routes
    public static final String APPROVED = "yes";
    public static final String REJECTED = "no";

    private String contentToApprove;
    private Set<String> by;
    private String additionalMessage;

    public ApprovalTask(Map<String, TaskProperty> properties) {
        this.contentToApprove = (String) properties.get(CONTENT_TO_APPROVE).getValue();
        this.by = getUserService().getUserIdsFromUserIdsAndRolesIds(
                getUserAndRolesIdsFromListOfIds((List<String>) properties.get(BY).getValue()));
        this.additionalMessage = (String) properties.get(ADDITIONAL_MESSAGE).getValue();
        userVariableNames = getUserVariableNamesFromListOfIds((List<String>) properties.get(BY).getValue());
    }

    @Setter
    private WorkflowComponent nextTaskForApproved;
    @Setter
    private WorkflowComponent nextTaskForRejected;

    @Override
    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        List<ReplyDTO> replies;
        List<Long> notificationIds = new ArrayList<>();
        by.addAll(getUserIdsFromInputParameters(inputParameters));
        try {
            FormRecordDTO formRecordDTO = (FormRecordDTO) inputParameters.get(contentToApprove);
            String correlationId = UUID.randomUUID().toString();
            by.forEach(userId -> {
                Long notificationId = createNotification(formRecordDTO, Long.valueOf(userId), additionalMessage,
                        NotificationType.APPROVAL, correlationId);
                notificationIds.add(notificationId);
            });
            replies = getTaskDispatcher().sendToManyAndWait(by, Map.of("type", NotificationType.APPROVAL.getLabel()), by.size(), correlationId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e); // TODO handle exception here better
        }
        setNextBasedOnApproves(replies, notificationIds);
//        System.out.println("Properties of " + TYPE + ": " + BY + ": " + String.join(",", by) + ", "
//                + CONTENT_TO_APPROVE + ": " + String.join(",", contentToApprove));
        return inputParameters;
    }

    private void setNextBasedOnApproves(List<ReplyDTO> replies, List<Long> notificationIds) {
        int numberOfApproves = replies.stream().filter(reply -> reply.getTextMessage().equals("approved")).toList().size();
        if (numberOfApproves == by.size()) {
            setNext(nextTaskForApproved);
            updateNotificationsAfterApproval(notificationIds, NotificationStatusType.APPROVED);
        } else {
            setNext(nextTaskForRejected);
            updateNotificationsAfterApproval(notificationIds, NotificationStatusType.REJECTED);
        }
    }

}
