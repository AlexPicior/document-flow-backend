package com.licenta.licenta.engine.notification.service;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.business.form.mapper.FormRecordMapper;
import com.licenta.licenta.business.form.model.FormRecord;
import com.licenta.licenta.business.form.repository.FormRecordRepository;
import com.licenta.licenta.business.form.service.FormRecordService;
import com.licenta.licenta.business.form.type.FormRecordType;
import com.licenta.licenta.engine.notification.dto.NotificationDTO;
import com.licenta.licenta.engine.notification.mapper.NotificationMapper;
import com.licenta.licenta.engine.notification.model.Notification;
import com.licenta.licenta.engine.notification.repository.NotificationRepository;
import com.licenta.licenta.engine.notification.type.NotificationStatusType;
import com.licenta.licenta.engine.notification.type.NotificationType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final FormRecordRepository formRecordRepository;
    private final FormRecordMapper formRecordMapper;

    public List<NotificationDTO> getNotificationsByTypeAndReceiverId(NotificationType type, Long receiverId) {
        return notificationMapper.toDTO(notificationRepository.findByTypeAndReceiverId(type, receiverId));
    }

    public NotificationDTO addNotification(NotificationDTO notificationDTO) {
        Notification notification = notificationMapper.toEntity(notificationDTO);
        FormRecord formRecord = notification.getFormRecord();
        if (formRecord != null) {
            formRecord.setFormRecordType(FormRecordType.NOTIFICATION);
        }
        return notificationMapper.toDTO(notificationRepository.save(notification));
    }

    public void deleteNotificationById(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public void updateNotificationAfterReply(Long notificationId, FormRecordDTO replyFormRecord) {
        replyFormRecord.setFormRecordType(FormRecordType.NOTIFICATION.getLabel());
        FormRecord formRecord = formRecordMapper.toEntity(replyFormRecord);
        formRecordRepository.save(formRecord);
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        notification.setFormRecord(formRecord);
        notification.setStatus(NotificationStatusType.RESPONDED);
        notificationRepository.save(notification);
    }

    public void updateNotificationAfterApproval(Long notificationId, NotificationStatusType status) {
        notificationRepository.updateNotificationStatus(notificationId, status);
    }
}
