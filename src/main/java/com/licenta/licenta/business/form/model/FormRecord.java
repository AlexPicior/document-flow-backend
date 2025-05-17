package com.licenta.licenta.business.form.model;

import com.licenta.licenta.business.form.type.FormRecordType;
import com.licenta.licenta.business.organisation.model.Organisation;
import com.licenta.licenta.security.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class FormRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "form_id", referencedColumnName = "id")
    private Form form;

    @ManyToOne
    @JoinColumn(name = "completed_by", referencedColumnName = "id")
    private User completedBy;

    @OneToMany(mappedBy = "formRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormFieldRecord> formFieldRecords;

    private LocalDate completedAt;

    private FormRecordType formRecordType;

    @ManyToOne
    @JoinColumn(name = "organisation_id", referencedColumnName = "id")
    private Organisation organisation;
}
