package com.example.loyalty.company.repository;

import com.example.loyalty.base.repository.BaseRepository;
import com.example.loyalty.company.domain.Company;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends BaseRepository<Company, Long> {
}