package com.licenta.licenta.security.controller;

import com.licenta.licenta.business.role.dto.RoleDTO;
import com.licenta.licenta.security.dto.UserDTO;
import com.licenta.licenta.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.base-path}/user/employee")
public class UserController {
    private final UserService userService;

    @GetMapping(path = "/organisation/{id}", produces = "application/json")
    public ResponseEntity<List<UserDTO>> getAllEmployeesByOrganisation(@PathVariable("id") Long organisationId) {
        return new ResponseEntity<>(userService.getAllUsersByOrganisationId(organisationId), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<UserDTO> saveEmployee(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.saveUser(userDTO));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("Deleted employee with id " + id);
    }
}
