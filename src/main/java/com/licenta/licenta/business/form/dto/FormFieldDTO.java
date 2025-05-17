package com.licenta.licenta.business.form.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FormFieldDTO {
    private Long id;

    private String label;

    private String type;

    private Integer pageNumber;

    private Integer index;

    private List<String> options;
}
