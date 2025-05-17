package com.licenta.licenta.engine.workflow.components;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.business.form.service.FormRecordService;
import com.licenta.licenta.business.form.service.FormService;
import com.licenta.licenta.engine.notification.TaskDispatcher;
import com.licenta.licenta.engine.notification.type.NotificationStatusType;
import com.licenta.licenta.engine.notification.type.NotificationType;
import com.licenta.licenta.engine.workflow.AbstractSendWorkflowComponent;
import com.licenta.licenta.engine.workflow.AbstractWorkflowComponent;
import com.licenta.licenta.engine.workflow.dto.ReplyDTO;
import com.licenta.licenta.engine.workflow.dto.task.TaskProperty;
import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import com.licenta.licenta.helper.SpringContextHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
@NoArgsConstructor
public class CompleteFormTask extends AbstractSendWorkflowComponent {
    public static final WorkflowComponentType TYPE = WorkflowComponentType.COMPLETE_FORM_TASK;

    public static final String FORM_TYPE = "formType";
    public static final String SEND_TO = "sendTo";
    public static final String COMPLETED_FORM = "completedForm";
    public static final String ADDITIONAL_MESSAGE = "additionalMessage";

    private String formType;
    private String sendTo;
    private String completedForm;
    private String additionalMessage;

    public CompleteFormTask(Map<String, TaskProperty> properties) {
        this.formType = (String) properties.get(FORM_TYPE).getValue();
        Set<String> userIds = getUserService().getUserIdsFromUserIdsAndRolesIds(
                getUserAndRolesIdsFromListOfIds(List.of((String) properties.get(SEND_TO).getValue())));
        this.sendTo = userIds.isEmpty() ? null : userIds.stream().findFirst().orElseThrow();
        this.completedForm = (String) properties.get(COMPLETED_FORM).getValue();
        this.additionalMessage = (String) properties.get(ADDITIONAL_MESSAGE).getValue();
        userVariableNames = getUserVariableNamesFromListOfIds(List.of((String) properties.get(SEND_TO).getValue()));
    }

    @Override
    protected Map<String, Object> executeInternal(Map<String, Object> inputParameters) {
        TaskDispatcher taskDispatcher = getTaskDispatcher();
        Map<String, Object> outputParameters = new HashMap<>(inputParameters);
        try {
            FormRecordDTO formRecordDTO = getFormRecordService().createEmptyFormRecordDTOFromFormId(Long.valueOf(formType));
            String receiverId = verifyAndGetSendTo(inputParameters);
            formRecordDTO.setUserId(Long.valueOf(receiverId));

            String correlationId = UUID.randomUUID().toString();
            Long notificationId = createNotification(formRecordDTO, Long.valueOf(receiverId), additionalMessage, NotificationType.REQUEST, correlationId);
            ReplyDTO reply = taskDispatcher.sendAndWaitForReply(verifyAndGetSendTo(inputParameters), Map.of("type", NotificationType.REQUEST.getLabel()), correlationId);
            updateNotificationAfterReply(notificationId, reply.getReplyForm());

            outputParameters.put(completedForm, reply.getReplyForm());
        } catch (InterruptedException e) {
            throw new RuntimeException(e); // TODO handle exception here better
        }
//        System.out.println("Properties of " + TYPE + ": " + FORM_TYPE + ": " + formType + ", "
//                + SEND_TO + ": " + sendTo + ", " + COMPLETED_FORM + ": " + completedForm);
        return outputParameters;
    }

    private String verifyAndGetSendTo(Map<String, Object> inputParameters) {
        if (sendTo == null) {
            return getUserIdsFromInputParameters(inputParameters).getFirst();
        }
        return sendTo;
    }

    private FormRecordService getFormRecordService() {
        return SpringContextHelper.getBean(FormRecordService.class);
    }
}
