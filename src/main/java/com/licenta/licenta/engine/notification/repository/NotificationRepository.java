package com.licenta.licenta.engine.notification.repository;

import com.licenta.licenta.engine.notification.model.Notification;
import com.licenta.licenta.engine.notification.type.NotificationStatusType;
import com.licenta.licenta.engine.notification.type.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByTypeAndReceiverId(NotificationType type, Long receiverId);

    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.status = :status WHERE n.id = :id")
    int updateNotificationStatus(Long id, NotificationStatusType status);
}
