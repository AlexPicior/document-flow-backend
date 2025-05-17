package com.licenta.licenta.engine.workflow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class WorkflowComponentPropertyConstantOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    private String value;

    @ManyToOne
    @JoinColumn(name = "workflow_component_property_id", referencedColumnName = "id", nullable = false)
    private WorkflowComponentProperty workflowComponentProperty;

}
