package com.example.book.model;

import lombok.Getter;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.math.BigDecimal;

@Getter
@ConstructorBinding
public class DiscountRule {

    private final String type;
    private final BigDecimal discount;

    public DiscountRule(String type, BigDecimal discount) {
        this.type = type;
        this.discount = discount;
    }
}
