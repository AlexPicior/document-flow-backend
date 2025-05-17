package com.licenta.licenta.engine.notification.controller;

import com.licenta.licenta.engine.notification.dto.NotificationDTO;
import com.licenta.licenta.engine.notification.service.NotificationService;
import com.licenta.licenta.engine.notification.type.NotificationType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.base-path}/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping(path = "/type/{type}/receiver/{receiverId}", produces = "application/json")
    public ResponseEntity<List<NotificationDTO>> getAllNotificationsByTypeAndReceiverId(@PathVariable("type") String type,
                                                                                        @PathVariable("receiverId") Long receiverId) {
        return new ResponseEntity<>(notificationService.
                getNotificationsByTypeAndReceiverId(NotificationType.fromString(type), receiverId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteNotification(@PathVariable("id") Long id) {
        notificationService.deleteNotificationById(id);
        return ResponseEntity.ok("Deleted notification with id " + id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
