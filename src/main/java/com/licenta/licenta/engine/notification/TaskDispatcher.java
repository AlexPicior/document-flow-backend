package com.licenta.licenta.engine.notification;

import com.licenta.licenta.engine.workflow.dto.ReplyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskDispatcher {

    private final ReplyGatherManager replyGatherManager;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotificationToUser(String userId, Map<String, Object> message) {
        //messagingTemplate.convertAndSendToUser(userId, "/queue/notify", message);
        messagingTemplate.convertAndSend("/topic/user." + userId, message);
    }

    public ReplyDTO sendAndWaitForReply(String userId, Map<String, Object> message, String correlationId) throws InterruptedException {
        return sendToManyAndWait(Set.of(userId), message, 1, correlationId).getFirst();
    }

    public List<ReplyDTO> sendToManyAndWait(Set<String> userIds, Map<String, Object> message,
                                            int expectedReplies, String correlationId) throws InterruptedException {
        replyGatherManager.register(correlationId, expectedReplies);

        for (String userId : userIds) {
            //messagingTemplate.convertAndSendToUser(userId, "/queue/notify", message);
            messagingTemplate.convertAndSend("/topic/user." + userId, message);
        }

        return replyGatherManager.waitForReplies(correlationId);
    }

    public void recordReply(String correlationId, ReplyDTO reply) {
        replyGatherManager.recordReply(correlationId, reply);
    }
}
