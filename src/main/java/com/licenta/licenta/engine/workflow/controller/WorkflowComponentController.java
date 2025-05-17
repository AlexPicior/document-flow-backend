package com.licenta.licenta.engine.workflow.controller;

import com.licenta.licenta.engine.workflow.dto.WorkflowComponentDTO;
import com.licenta.licenta.engine.workflow.service.WorkflowComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.base-path}/workflowComponent")
public class WorkflowComponentController {
    private final WorkflowComponentService workflowComponentService;

    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<List<WorkflowComponentDTO>> getAllWorkflowComponents() {
        return ResponseEntity.ok(workflowComponentService.getAllWorkflowComponents());
    }

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<WorkflowComponentDTO> createWorkflowComponent(@RequestBody WorkflowComponentDTO workflowComponentDTO) {
        return ResponseEntity.ok(workflowComponentService.createWorkflowComponent(workflowComponentDTO));
    }

    @PostMapping(path = "/all", consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<WorkflowComponentDTO>> createAllWorkflowComponent(@RequestBody List<WorkflowComponentDTO> workflowComponentDTOs) {
        return ResponseEntity.ok(workflowComponentService.createAllWorkflowComponent(workflowComponentDTOs));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteWorkflowComponent(@PathVariable("id") Long id) {
        workflowComponentService.deleteWorkflowComponent(id);
        return ResponseEntity.ok("Deleted workflow component with id " + id);
    }
}
