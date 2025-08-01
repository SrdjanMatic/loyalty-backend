package com.example.loyalty.company.controller;

import com.example.loyalty.company.domain.Company;
import com.example.loyalty.company.domain.CompanyDTO;
import com.example.loyalty.company.service.CompanyServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyServiceImpl service;

    @PostMapping
    public Company create(@RequestBody @Valid CompanyDTO companyDTO, Principal principal) {
        return service.create(companyDTO,principal);
    }
    @GetMapping()
    public List<Company> getAll(Principal principal) {
        return service.findAll(principal);
    }

}
