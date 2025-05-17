package com.licenta.licenta.engine.notification.model;

import com.licenta.licenta.business.form.model.FormRecord;
import com.licenta.licenta.engine.notification.type.NotificationStatusType;
import com.licenta.licenta.engine.notification.type.NotificationType;
import com.licenta.licenta.security.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "form_record", referencedColumnName = "id")
    private FormRecord formRecord;

    private LocalDateTime sentAt;

    private NotificationStatusType status;

    private NotificationType type;

    private String correlationId;

    @ManyToOne
    @JoinColumn(name = "receiver", referencedColumnName = "id")
    private User receiver;
}
