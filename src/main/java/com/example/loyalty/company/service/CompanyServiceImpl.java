package com.example.loyalty.company.service;

import com.example.loyalty.company.domain.Company;
import com.example.loyalty.company.domain.CompanyDTO;
import com.example.loyalty.company.domain.CompanyView;
import com.example.loyalty.company.repository.CompanyRepository;
import com.example.loyalty.company.security.CompanyRolePermissionChecker;
import com.example.loyalty.employee.domain.EmployeeView;
import com.example.loyalty.employee.repository.EmployeeRepository;
import com.example.loyalty.security.service.KeycloakService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final KeycloakService keycloakService;
    private final CompanyRolePermissionChecker rolePermissionsChecker;

    @Override
    public List<CompanyView> findAll(Principal principal) {
        rolePermissionsChecker.canViewAllCompanies(principal);
        return companyRepository.findAllBy(CompanyView.class);
    }

    @Override
    @Transactional
    public CompanyView create(CompanyDTO companyDTO, Principal principal) {
        rolePermissionsChecker.canCreateOrUpdateCompany(principal);
        Company company = buildCompany(companyDTO);
        Company saved = companyRepository.save(company);
        return companyRepository.findById(saved.getId(), CompanyView.class)
                .orElseThrow(() -> new IllegalStateException("Saved company not found"));
    }

    @Override
    @Transactional
    public CompanyView update(Long id, CompanyDTO companyDTO, Principal principal) {
        rolePermissionsChecker.canCreateOrUpdateCompany(principal);

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));

        company.setName(companyDTO.name());
        company.setUpdatedAt(LocalDateTime.now());

        Company updatedCompany = companyRepository.save(company);

        return companyRepository.findById(updatedCompany.getId(), CompanyView.class)
                .orElseThrow(() -> new IllegalStateException("Updated company not found"));

    }

    @Override
    @Transactional
    public void delete(Long id, Principal principal) {
        rolePermissionsChecker.canDeleteCompany(principal);

        List<EmployeeView> employees = employeeRepository.findByCompanyId(id);

        for (EmployeeView employee : employees) {
            keycloakService.assignVipCardByEmail(employee.getEmail(), "false");
            employeeRepository.deleteById(employee.getId());
        }
        companyRepository.deleteById(id);
    }

    private static Company buildCompany(CompanyDTO companyDTO) {
        return Company.builder()
                .name(companyDTO.name())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
