package com.licenta.licenta.engine.workflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TriggerKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String personKey;

    private String formKey;

    @ManyToOne
    @JoinColumn(name = "workflow_definition_id", referencedColumnName = "id", nullable = false)
    private WorkflowDefinition workflowDefinition;
}
