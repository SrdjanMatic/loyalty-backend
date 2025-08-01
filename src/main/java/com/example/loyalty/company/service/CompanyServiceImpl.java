package com.example.loyalty.company.service;

import com.example.loyalty.company.domain.Company;
import com.example.loyalty.company.domain.CompanyDTO;
import com.example.loyalty.company.repository.CompanyRepository;
import com.example.loyalty.company.security.CompanyRolePermissionChecker;
import com.example.loyalty.restaurant.security.RestaurantRolePermissionChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyRolePermissionChecker rolePermissionsChecker;

    @Override
    public List<Company> findAll(Principal principal) {
        rolePermissionsChecker.canViewAllCompanies(principal);
        return companyRepository.findAll();
    }

    @Override
    @Transactional
    public Company create(CompanyDTO companyDTO, Principal principal) {
        rolePermissionsChecker.canCreateNewCompany(principal);
        Company company = buildCompany(companyDTO);
        return companyRepository.save(company);
    }

    private static Company buildCompany(CompanyDTO companyDTO) {
        return Company.builder()
                .name(companyDTO.name())
                .build();
    }
}
