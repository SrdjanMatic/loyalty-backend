package com.example.loyalty.employee.service;

import com.example.loyalty.company.domain.Company;
import com.example.loyalty.company.repository.CompanyRepository;
import com.example.loyalty.employee.domain.Employee;
import com.example.loyalty.employee.domain.EmployeeDTO;
import com.example.loyalty.employee.domain.EmployeeView;
import com.example.loyalty.employee.repository.EmployeeRepository;
import com.example.loyalty.employee.security.EmployeeRolePermissionChecker;
import com.example.loyalty.security.service.KeycloakService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final KeycloakService keycloakService;
    private final EmployeeRolePermissionChecker employeeRolePermissionChecker;


    @Override
    public List<EmployeeView> findAllByCompanyId(Long companyId, Principal principal) {
        employeeRolePermissionChecker.canViewAllEmployees(principal);
        return employeeRepository.findByCompanyId(companyId);
    }

    @Override
    @Transactional
    public Employee create(EmployeeDTO employeeDTO, Principal principal) {
        employeeRolePermissionChecker.canCreateEmployee(principal);
        Employee employee = buildEmployee(employeeDTO);
        Employee newEmployee = employeeRepository.save(employee);
        keycloakService.assignVipCardByEmail(newEmployee.getEmail(), "true");
        return newEmployee;
    }

    @Override
    @Transactional
    public void deleteEmployee(Long employeeId, Principal principal) {
        employeeRolePermissionChecker.canDeleteEmployee(principal);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee not found with id:" + employeeId));
        keycloakService.assignVipCardByEmail(employee.getEmail(), "false");
        employeeRepository.deleteById(employeeId);
    }

    private Employee buildEmployee(EmployeeDTO employeeDTO) {
        Company company = companyRepository.findById(employeeDTO.companyId()).orElseThrow(() -> new EntityNotFoundException("Company not found with id:" + employeeDTO.companyId()));
        return Employee.builder()
                .firstName(employeeDTO.firstName())
                .lastName(employeeDTO.lastName())
                .email(employeeDTO.email())
                .company(company)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
