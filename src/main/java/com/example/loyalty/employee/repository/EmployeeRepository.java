package com.example.loyalty.employee.repository;

import com.example.loyalty.employee.domain.Employee;
import com.example.loyalty.employee.domain.EmployeeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<EmployeeView> findByCompanyId(Long companyId);
}
