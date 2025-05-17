package com.licenta.licenta.engine.workflow.model;

import com.licenta.licenta.engine.workflow.type.WorkflowComponentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class WorkflowComponentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    private WorkflowComponentType type;

    private String description;

    @OneToMany(mappedBy="workflowComponent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowComponentProperty> properties;
}
