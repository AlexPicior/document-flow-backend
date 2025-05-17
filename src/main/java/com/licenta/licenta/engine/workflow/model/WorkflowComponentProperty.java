package com.licenta.licenta.engine.workflow.model;

import com.licenta.licenta.engine.workflow.type.InitialOptionsType;
import com.licenta.licenta.engine.workflow.type.TaskPropertyType;
import com.licenta.licenta.engine.workflow.type.VariableType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class WorkflowComponentProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer orderNumber;

    private String propertyKey;

    @ManyToOne
    @JoinColumn(name = "workflow_component_id", referencedColumnName = "id")
    private WorkflowComponentModel workflowComponent;

    private String label;

    private String description;

    private TaskPropertyType type;

    private VariableType variableType;

    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private WorkflowComponentProperty parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowComponentProperty> nextProperties;

    @OneToMany(mappedBy="workflowComponentProperty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowComponentPropertyConstantOption> constantOptions;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "int[]")
    private InitialOptionsType[] initialOptionsTypes;

    private VariableType optionsType;

}
