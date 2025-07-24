package com.example.loyalty.company.service;

import com.example.loyalty.company.domain.Company;
import com.example.loyalty.company.domain.CompanyDTO;

import java.util.List;

public interface CompanyService {
    List<Company> findAll();

    Company create(CompanyDTO companyDTO);
}
