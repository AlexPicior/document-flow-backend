package com.licenta.licenta.business.role.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoleDTO {
    private Long id;

    private String label;

    private Long organisationId;
}
