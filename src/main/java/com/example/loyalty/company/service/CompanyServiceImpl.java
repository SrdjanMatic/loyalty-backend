package com.example.loyalty.company.service;

import com.example.loyalty.company.domain.Company;
import com.example.loyalty.company.domain.CompanyDTO;
import com.example.loyalty.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService{

    private final CompanyRepository companyRepository;

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Company create(CompanyDTO companyDTO) {
       Company company = buildCompany(companyDTO);
       return companyRepository.save(company);
    }

    private static Company buildCompany(CompanyDTO companyDTO) {
        return Company.builder()
                .name(companyDTO.name())
                .build();
    }
}
