package com.example.book.config;

import com.example.book.model.PromotionRule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "book.discount")
public class DiscountConfig {

    private List<PromotionRule> promotionRules;
}
