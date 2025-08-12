package com.example.loyalty.company.service;

import com.example.loyalty.company.domain.CompanyDTO;
import com.example.loyalty.company.domain.CompanyView;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;

public interface CompanyService {
    List<CompanyView> findAll(Principal principal);

    CompanyView create(CompanyDTO companyDTO, Principal principal);

    CompanyView update(Long id, @Valid CompanyDTO companyDTO, Principal principal);

    void delete(Long id, Principal principal);
}
