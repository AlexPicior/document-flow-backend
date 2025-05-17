package com.licenta.licenta.business.form.controller;

import com.licenta.licenta.business.form.dto.FormRecordDTO;
import com.licenta.licenta.business.form.service.FormRecordService;
import com.licenta.licenta.engine.notification.type.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.base-path}/formRecord")
public class FormRecordController {
    private final FormRecordService formRecordService;

    @GetMapping(path = "/storage/organisation/{organisationId}", produces = "application/json")
    public ResponseEntity<List<FormRecordDTO>> getAllFormRecordsForStorage(@PathVariable("organisationId") Long organisationId) {
        return new ResponseEntity<>(formRecordService.findAllForStorage(organisationId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteFormRecord(@PathVariable("id") Long id) {
        formRecordService.deleteFormRecord(id);
        return ResponseEntity.ok("Deleted form record with id " + id);
    }
}
