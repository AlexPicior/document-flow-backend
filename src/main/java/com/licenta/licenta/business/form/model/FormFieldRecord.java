package com.licenta.licenta.business.form.model;

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
public class FormFieldRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "form_field_id", referencedColumnName = "id")
    private FormField formField;

    private String value;

    @OneToMany(mappedBy = "formFieldRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormFieldRecordArrayValue> arrayValues;

    @ManyToOne
    @JoinColumn(name = "form_record_id", referencedColumnName = "id")
    private FormRecord formRecord;
}
