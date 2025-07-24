package com.example.loyalty.company.controller;

import com.example.loyalty.company.domain.Company;
import com.example.loyalty.company.domain.CompanyDTO;
import com.example.loyalty.company.service.CompanyServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyServiceImpl service;

    @PostMapping
    public ResponseEntity<Company> create(@RequestBody CompanyDTO companyDTO, Principal principal) {
        return ResponseEntity.ok(service.create(companyDTO));
    }

    @GetMapping()
    public ResponseEntity<List<Company>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }
}
