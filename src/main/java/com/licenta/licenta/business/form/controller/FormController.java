package com.licenta.licenta.business.form.controller;

import com.licenta.licenta.business.form.dto.FormDTO;
import com.licenta.licenta.business.form.service.FormService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.base-path}/form")
public class FormController {
    private final FormService formService;

    @GetMapping(path = "/organisation/{id}", produces = "application/json")
    public ResponseEntity<List<FormDTO>> getAllFormsByOrganisation(@PathVariable("id") Long organisationId) {
        return new ResponseEntity<>(formService.getAllByOrganisationId(organisationId), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<FormDTO> getForm(@PathVariable("id") Long id) {
        return new ResponseEntity<>(formService.getFormById(id), HttpStatus.OK);
    }

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<FormDTO> saveForm(@RequestBody FormDTO formDTO) {
        return ResponseEntity.ok(formService.saveForm(formDTO));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteForm(@PathVariable("id") Long id) {
        formService.deleteByFormId(id);
        return ResponseEntity.ok("Deleted workflow component with id " + id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
