package com.example.loyalty.company.controller;

import com.example.loyalty.company.domain.CompanyDTO;
import com.example.loyalty.company.domain.CompanyView;
import com.example.loyalty.company.service.CompanyServiceImpl;
import com.example.loyalty.employee.domain.EmployeeUpdateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyServiceImpl service;

    @PostMapping
    public CompanyView create(@RequestBody @Valid CompanyDTO companyDTO, Principal principal) {
        return service.create(companyDTO,principal);
    }

    @PutMapping("/{id}")
    public CompanyView update(@PathVariable Long id,
                               @RequestBody @Valid CompanyDTO companyDTO, Principal principal) {
        return service.update(id, companyDTO, principal);
    }
    @GetMapping
    public List<CompanyView> getAll(Principal principal) {
        return service.findAll(principal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        service.delete(id, principal);
        return ResponseEntity.noContent().build();
    }
}
