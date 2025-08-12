package com.example.loyalty.employee.service;

import com.example.loyalty.employee.domain.EmployeeCreateDTO;
import com.example.loyalty.employee.domain.EmployeeUpdateDTO;
import com.example.loyalty.employee.domain.EmployeeView;

import java.security.Principal;
import java.util.List;

public interface EmployeeService {
    List<EmployeeView> findAllByCompanyId(Long companyId, Principal principal);

    EmployeeView create(EmployeeCreateDTO employeeDTO, Principal principal);

    void deleteEmployee(Long employeeId, Principal principal);

    EmployeeView update(Long id, EmployeeUpdateDTO employeeDTO, Principal principal);
}