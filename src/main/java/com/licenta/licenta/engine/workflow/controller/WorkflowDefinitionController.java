package com.licenta.licenta.engine.workflow.controller;

import com.licenta.licenta.engine.workflow.dto.WorkflowDefinitionDTO;
import com.licenta.licenta.engine.workflow.service.WorkflowDefinitionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.base-path}/workflowDefinition")
public class WorkflowDefinitionController {
    private final WorkflowDefinitionService workflowDefinitionService;

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<WorkflowDefinitionDTO> getWorkflowDefinition(@PathVariable("id") Long id) {
        return new ResponseEntity<>(workflowDefinitionService.getWorkflowDefinitionById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/organisation/{organisationId}", produces = "application/json")
    public ResponseEntity<List<WorkflowDefinitionDTO>> getAllWorkflowDefinitionsByOrganisation(@PathVariable("organisationId") Long organisationId) {
        return new ResponseEntity<>(workflowDefinitionService.getWorkflowDefinitionsByOrganizationId(organisationId), HttpStatus.OK);
    }

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<WorkflowDefinitionDTO> createWorkflowDefinition(@RequestBody WorkflowDefinitionDTO workflowDefinitionDTO) {
        return new ResponseEntity<>(workflowDefinitionService.createWorkflowDefinition(workflowDefinitionDTO), HttpStatus.OK);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteWorkflowDefinition(@PathVariable("id") Long id) {
        workflowDefinitionService.deleteWorkflowDefinitionById(id);
        return ResponseEntity.ok("Deleted workflow definition with id " + id);
    }
}
