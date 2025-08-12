package com.example.loyalty.company.domain;

import com.example.loyalty.base.domain.AbstractBaseEntity;
import com.example.loyalty.restaurant.domain.Restaurant;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Company extends AbstractBaseEntity {

    private String name;

}