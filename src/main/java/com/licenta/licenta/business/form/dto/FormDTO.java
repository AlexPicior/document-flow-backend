package com.licenta.licenta.business.form.dto;

import com.licenta.licenta.security.dto.UserDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FormDTO {
    private Long id;

    private String lastModifiedById;

    private String label;

    private String description;

    private LocalDateTime lastModifiedAt;

    private UserDTO lastModifiedBy;

    private Long organisationId;

    private List<List<FormFieldDTO>> pages;
}
