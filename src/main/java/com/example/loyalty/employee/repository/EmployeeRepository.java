package com.example.loyalty.employee.repository;

import com.example.loyalty.base.repository.BaseRepository;
import com.example.loyalty.employee.domain.Employee;
import com.example.loyalty.employee.domain.EmployeeView;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends BaseRepository<Employee, Long> {
    List<EmployeeView> findByCompanyId(Long companyId);
}
