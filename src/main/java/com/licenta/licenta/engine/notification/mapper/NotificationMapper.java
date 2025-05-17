package com.licenta.licenta.engine.notification.mapper;

import com.licenta.licenta.business.form.mapper.FormRecordMapper;
import com.licenta.licenta.engine.notification.dto.NotificationDTO;
import com.licenta.licenta.engine.notification.model.Notification;
import com.licenta.licenta.engine.notification.type.NotificationStatusType;
import com.licenta.licenta.engine.notification.type.NotificationType;
import com.licenta.licenta.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationMapper {
    private final FormRecordMapper formRecordMapper;
    private final UserService userService;

    public Notification toEntity(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setId(notificationDTO.getId());
        notification.setMessage(notificationDTO.getMessage());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        notification.setSentAt(LocalDateTime.parse(notificationDTO.getSentAt(), formatter));
        notification.setStatus(NotificationStatusType.fromString(notificationDTO.getStatus()));
        notification.setType(NotificationType.fromString(notificationDTO.getType()));
        notification.setFormRecord(notificationDTO.getFormRecord() != null ? formRecordMapper.toEntity(notificationDTO.getFormRecord()) : null);
        notification.setReceiver(userService.getUserById(notificationDTO.getReceiverId()));
        notification.setCorrelationId(notificationDTO.getCorrelationId());
        return notification;
    }

    public NotificationDTO toDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setSentAt(notification.getSentAt().toString());
        notificationDTO.setStatus(notification.getStatus().getLabel());
        notificationDTO.setType(notification.getType().getLabel());
        notificationDTO.setFormRecord(notification.getFormRecord() != null ? formRecordMapper.toDTO(notification.getFormRecord()) : null);
        notificationDTO.setReceiverId(notification.getReceiver().getId());
        notificationDTO.setCorrelationId(notification.getCorrelationId());
        return notificationDTO;
    }

    public List<NotificationDTO> toDTO(List<Notification> notifications) {
        return notifications.stream().map(this::toDTO).toList();
    }

}
