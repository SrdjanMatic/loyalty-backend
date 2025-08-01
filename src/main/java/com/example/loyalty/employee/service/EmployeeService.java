package com.example.loyalty.employee.service;

import com.example.loyalty.employee.domain.Employee;
import com.example.loyalty.employee.domain.EmployeeDTO;
import com.example.loyalty.employee.domain.EmployeeView;

import java.security.Principal;
import java.util.List;

public interface EmployeeService {
    List<EmployeeView> findAllByCompanyId(Long companyId, Principal principal);

    Employee create(EmployeeDTO employeeDTO, Principal principal);

    void deleteEmployee(Long employeeId, Principal principal);
}