package com.licenta.licenta.business.form.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FormFieldRecordDTO {
    private Long id;
    private FormFieldDTO formField;
    private String value;
    private List<String> arrayValues;
}
