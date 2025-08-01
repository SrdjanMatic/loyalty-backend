package com.example.loyalty.employee.controller;

import com.example.loyalty.employee.domain.Employee;
import com.example.loyalty.employee.domain.EmployeeDTO;
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
    public Employee create(@RequestBody @Valid EmployeeDTO employeeDTO, Principal principal) {
        return service.create(employeeDTO, principal);
    }

    @GetMapping("/{companyId}")
    public List<EmployeeView> getAll(@PathVariable Long companyId, Principal principal) {
        return service.findAllByCompanyId(companyId, principal);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId, Principal principal) {
        service.deleteEmployee(employeeId, principal);
        return ResponseEntity.noContent().build();
    }
}