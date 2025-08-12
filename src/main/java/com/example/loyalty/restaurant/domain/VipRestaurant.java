package com.example.loyalty.restaurant.domain;

import com.example.loyalty.base.domain.AbstractBaseEntity;
import com.example.loyalty.coupon.domain.Coupon;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VipRestaurant extends AbstractBaseEntity {

    @OneToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private BigDecimal generalDiscount;

    private String backgroundImage;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "vip_restaurant_discount",
            joinColumns = @JoinColumn(name = "vip_restaurant_id")
    )
    @MapKeyColumn(name = "level")
    @Column(name = "discount")
    @MapKeyEnumerated(EnumType.STRING)
    private Map<Coupon.Level, BigDecimal> levelDiscounts;

}