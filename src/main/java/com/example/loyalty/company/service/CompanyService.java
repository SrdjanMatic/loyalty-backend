package com.example.loyalty.company.service;

import com.example.loyalty.company.domain.Company;
import com.example.loyalty.company.domain.CompanyDTO;

import java.security.Principal;
import java.util.List;

public interface CompanyService {
    List<Company> findAll(Principal principal);

    Company create(CompanyDTO companyDTO, Principal principal);
}
