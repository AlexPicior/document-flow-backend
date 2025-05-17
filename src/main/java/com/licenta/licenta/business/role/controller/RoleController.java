package com.licenta.licenta.business.role.controller;

import com.licenta.licenta.business.form.dto.FormDTO;
import com.licenta.licenta.business.role.dto.RoleDTO;
import com.licenta.licenta.business.role.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.base-path}/role")
public class RoleController {
    private final RoleService roleService;

    @GetMapping(path = "/organisation/{id}", produces = "application/json")
    public ResponseEntity<List<RoleDTO>> getAllRolesByOrganisation(@PathVariable("id") Long organisationId) {
        return new ResponseEntity<>(roleService.getAllRolesByOrganisationId(organisationId), HttpStatus.OK);
    }

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<RoleDTO> saveRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.saveRole(roleDTO));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRoleById(id);
        return ResponseEntity.ok("Deleted role with id " + id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
