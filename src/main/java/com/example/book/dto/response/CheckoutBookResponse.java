package com.example.book.dto.response;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutBookResponse {

    @NotNull
    private BigDecimal originalPrice;
    private BigDecimal priceAfterDiscount;
}
