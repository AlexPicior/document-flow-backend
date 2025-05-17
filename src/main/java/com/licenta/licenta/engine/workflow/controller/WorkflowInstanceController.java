package com.licenta.licenta.engine.workflow.controller;

import com.licenta.licenta.engine.workflow.dto.TriggerWorkflowRequestDTO;
import com.licenta.licenta.engine.workflow.service.WorkflowInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.base-path}/workflowInstance")
public class WorkflowInstanceController {
    private final WorkflowInstanceService workflowInstanceService;

    @PostMapping(path = "/trigger", consumes = "application/json")
    public ResponseEntity<String> triggerWorkflow(@RequestBody TriggerWorkflowRequestDTO triggerWorkflowRequestDTO) {
        workflowInstanceService.triggerWorkflowInstances(triggerWorkflowRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
