package com.licenta.licenta.engine.notification;

import com.licenta.licenta.engine.workflow.dto.ReplyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.base-path}/reply")
@RequiredArgsConstructor
public class TaskReplyController {
    private final TaskDispatcher taskDispatcher;

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> replyToTask(@RequestBody ReplyDTO replyDTO) {

        taskDispatcher.recordReply(replyDTO.getCorrelationId(), replyDTO);

        return ResponseEntity.ok().build();
    }

}
