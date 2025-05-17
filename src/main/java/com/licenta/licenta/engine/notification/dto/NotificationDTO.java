package com.licenta.licenta.engine.notification.dto;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {
    private Long id;

    private String type;

    private String message;

    private FormRecordDTO formRecord;

    private String sentAt;

    private String status;

    private Long receiverId;

    private String correlationId;
}
