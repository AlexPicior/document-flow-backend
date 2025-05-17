package com.licenta.licenta.business.form.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class FormField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    private String type;

    private Integer pageNumber;

    private Integer index;

    @OneToMany(mappedBy = "formField", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormFieldOption> options;

    @ManyToOne
    @JoinColumn(name = "form_id", referencedColumnName = "id")
    private Form form;
}
