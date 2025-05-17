package com.licenta.licenta.engine.workflow.model;

import com.licenta.licenta.business.organisation.model.Organisation;
import com.licenta.licenta.engine.workflow.dto.WorkflowJSONDefinition;
import com.licenta.licenta.security.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class WorkflowDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User lastModifiedBy;

    @ManyToOne
    @JoinColumn(name = "organisation_id", referencedColumnName = "id", nullable = false)
    private Organisation organisation;

    private String label;

    private String description;

    private LocalDateTime lastModifiedAt;

    @OneToMany(mappedBy="workflowDefinition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TriggerKey> triggerKeys;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private WorkflowJSONDefinition jsonDefinition;

}
