package com.example.loyalty.employee.domain;

import com.example.loyalty.base.domain.AbstractBaseEntity;
import com.example.loyalty.company.domain.Company;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Employee extends AbstractBaseEntity {

    private String email;

    private String firstName;

    private String lastName;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;


}