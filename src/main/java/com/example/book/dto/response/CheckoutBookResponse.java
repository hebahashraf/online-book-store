package com.example.book.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Total price of all the books checked out without discount", required = true, example = "1000.00")
    @NotNull
    private BigDecimal originalPrice;

    @Schema(description = "Total price of all the books checked out after discount", required = true, example = "950.00")
    @NotNull
    private BigDecimal priceAfterDiscount;
}
