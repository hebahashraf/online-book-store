package com.example.book.model;

import lombok.Getter;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;


@ConstructorBinding
@Getter
public class PromotionRule {

    private final String promotionCode;
    private final List<DiscountRule> discountRules;

    public PromotionRule(String promotionCode, List<DiscountRule> discountRules) {
        this.promotionCode = promotionCode;
        this.discountRules = discountRules;
    }
}
