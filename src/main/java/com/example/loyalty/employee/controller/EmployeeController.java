package com.example.loyalty.employee.controller;

import com.example.loyalty.employee.domain.EmployeeCreateDTO;
import com.example.loyalty.employee.domain.EmployeeUpdateDTO;
import com.example.loyalty.employee.domain.EmployeeView;
import com.example.loyalty.employee.service.EmployeeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeServiceImpl service;

    @PostMapping
    public EmployeeView create(@RequestBody @Valid EmployeeCreateDTO employeeDTO, Principal principal) {
        return service.create(employeeDTO, principal);
    }

    @PutMapping("/{id}")
    public EmployeeView update(@PathVariable Long id,
                               @RequestBody @Valid EmployeeUpdateDTO employeeDTO, Principal principal) {
        return service.update(id, employeeDTO, principal);
    }

    @GetMapping
    public List<EmployeeView> getAll(@RequestParam("companyId") Long companyId, Principal principal) {
        return service.findAllByCompanyId(companyId, principal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id, Principal principal) {
        service.deleteEmployee(id, principal);
        return ResponseEntity.noContent().build();
    }
}