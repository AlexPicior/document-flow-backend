package com.licenta.licenta.engine.notification;

import com.licenta.licenta.engine.workflow.dto.ReplyDTO;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
public class ReplyGatherManager {
    private final Map<String, GatherReplyState> taskReplyStates = new ConcurrentHashMap<>();

    public void register(String correlationId, int expectedReplies) {
        taskReplyStates.put(correlationId, new GatherReplyState(expectedReplies));
    }

    public void recordReply(String correlationId, ReplyDTO payload) {
        GatherReplyState state = taskReplyStates.get(correlationId);
        if (state != null) {
            state.addReply(payload);
        }
    }

    public List<ReplyDTO> waitForReplies(String correlationId) throws InterruptedException {
        GatherReplyState state = taskReplyStates.get(correlationId);
        if (state == null) {
            return List.of();
        }
        boolean success = state.await();
        taskReplyStates.remove(correlationId);
        return success ? state.getReplies() : List.of();
    }

    static class GatherReplyState {
        private final CountDownLatch latch;
        @Getter
        private final List<ReplyDTO> replies = Collections.synchronizedList(new ArrayList<>());

        public GatherReplyState(int expectedReplies) {
            this.latch = new CountDownLatch(expectedReplies);
        }

        public void addReply(ReplyDTO reply) {
            replies.add(reply);
            latch.countDown();
        }

        public boolean await() throws InterruptedException {
            long EXPIRATION_TIME_MS = 2_592_000_000L; // 30 days
            return latch.await(EXPIRATION_TIME_MS, TimeUnit.MILLISECONDS);
        }

    }
}
