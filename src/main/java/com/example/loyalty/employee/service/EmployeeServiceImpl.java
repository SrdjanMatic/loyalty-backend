package com.example.loyalty.employee.service;

import com.example.loyalty.company.domain.Company;
import com.example.loyalty.company.repository.CompanyRepository;
import com.example.loyalty.employee.domain.Employee;
import com.example.loyalty.employee.domain.EmployeeCreateDTO;
import com.example.loyalty.employee.domain.EmployeeUpdateDTO;
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
public class EmployeeServiceImpl implements EmployeeService {

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
    public EmployeeView create(EmployeeCreateDTO employeeDTO, Principal principal) {
        employeeRolePermissionChecker.canCreateOrUpdateEmployee(principal);
        Employee employee = buildEmployee(employeeDTO);
        Employee newEmployee = employeeRepository.save(employee);
        keycloakService.assignVipCardByEmail(newEmployee.getEmail(), "true");

        return employeeRepository.findById(newEmployee.getId(), EmployeeView.class)
                .orElseThrow(() -> new IllegalStateException("Saved employee not found"));

    }

    @Override
    @Transactional
    public EmployeeView update(Long id, EmployeeUpdateDTO employeeDTO, Principal principal) {
        employeeRolePermissionChecker.canCreateOrUpdateEmployee(principal);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));

        if (!employee.getCompany().getId().equals(employeeDTO.companyId())) {
            throw new IllegalStateException("Company can not be changed");
        }

        employee.setFirstName(employeeDTO.firstName());
        employee.setLastName(employeeDTO.lastName());
        employee.setUpdatedAt(LocalDateTime.now());

        Employee updatedEmployee = employeeRepository.save(employee);

        return employeeRepository.findById(updatedEmployee.getId(), EmployeeView.class)
                .orElseThrow(() -> new IllegalStateException("Updated employee not found"));

    }

    @Override
    @Transactional
    public void deleteEmployee(Long employeeId, Principal principal) {
        employeeRolePermissionChecker.canDeleteEmployee(principal);
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new EntityNotFoundException("Employee not found with id:" + employeeId));
        keycloakService.assignVipCardByEmail(employee.getEmail(), "false");
        employeeRepository.deleteById(employeeId);
    }


    private Employee buildEmployee(EmployeeCreateDTO employeeDTO) {
        Company company = companyRepository.findById(employeeDTO.companyId()).orElseThrow(() -> new EntityNotFoundException("Company not found with id:" + employeeDTO.companyId()));
        return Employee.builder()
                .firstName(employeeDTO.firstName())
                .lastName(employeeDTO.lastName())
                .email(employeeDTO.email())
                .company(company)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
