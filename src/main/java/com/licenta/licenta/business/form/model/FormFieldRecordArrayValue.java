package com.licenta.licenta.business.form.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class FormFieldRecordArrayValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @ManyToOne
    @JoinColumn(name = "form_field_record_id", referencedColumnName = "id")
    private FormFieldRecord formFieldRecord;
}
