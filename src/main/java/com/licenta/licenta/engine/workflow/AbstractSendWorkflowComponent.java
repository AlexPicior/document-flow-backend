package com.licenta.licenta.engine.workflow;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.business.form.type.FormRecordType;
import com.licenta.licenta.engine.notification.dto.NotificationDTO;
import com.licenta.licenta.engine.notification.service.NotificationService;
import com.licenta.licenta.engine.notification.type.NotificationStatusType;
import com.licenta.licenta.engine.notification.type.NotificationType;
import com.licenta.licenta.helper.SpringContextHelper;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class AbstractSendWorkflowComponent extends AbstractWorkflowComponent {
    protected Set<String> userVariableNames;

    protected List<String> getUserIdsFromInputParameters(Map<String, Object> inputParameters) {
        List<String> userIds = new ArrayList<>();
        userVariableNames.forEach(variableName -> {
            String userId = (String) inputParameters.get(variableName);
            if (userId != null) {
                userIds.add(userId);
            }
        });
        return userIds;
    }

    protected Set<String> getUserVariableNamesFromListOfIds(List<String> ids) {
        return ids.stream().filter(id -> !isNumeric(id.substring(0, id.length() - 1))).collect(Collectors.toCollection(HashSet::new));
    }

    protected Set<String> getUserAndRolesIdsFromListOfIds(List<String> ids) {
        return ids.stream().filter(id -> isNumeric(id.substring(0, id.length() - 1))).collect(Collectors.toCollection(HashSet::new));
    }

    private static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    protected Long createNotification(FormRecordDTO content, Long receiverId, String additionalMessage, NotificationType type, String correlationId) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setMessage(additionalMessage);
        notificationDTO.setStatus(NotificationStatusType.WAITING.getLabel());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        notificationDTO.setSentAt(LocalDateTime.now().format(formatter));
        notificationDTO.setType(type.getLabel());
        notificationDTO.setReceiverId(receiverId);
        notificationDTO.setCorrelationId(correlationId);
        if (content != null) {
            content.setFormRecordType(FormRecordType.NOTIFICATION.getLabel());
            content.setId(null);
            content.getFieldRecords().stream().peek(formFieldRecordDTO -> formFieldRecordDTO.setId(null)).collect(Collectors.toList());
        }
        notificationDTO.setFormRecord(content);
        return getNotificationService().addNotification(notificationDTO).getId();
    }

    protected void updateNotificationAfterReply(Long notificationId, FormRecordDTO replyFormRecord) {
        getNotificationService().updateNotificationAfterReply(notificationId, replyFormRecord);
    }

    protected void updateNotificationsAfterApproval(List<Long> notificationIds, NotificationStatusType status) {
        NotificationService notificationService = getNotificationService();
        notificationIds.forEach(notificationId -> notificationService.updateNotificationAfterApproval(notificationId, status));
    }

    protected NotificationService getNotificationService() {
        return SpringContextHelper.getBean(NotificationService.class);
    }
}
