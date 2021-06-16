package com.example.book.dto.request;

import com.example.book.model.CheckedOutBook;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class CheckoutBookRequestDto {

    @Schema(description = "promo code to avail discount", example = "PROMO2021")
    private String promoCode;

    @Schema(description = "the items to be checked out", required = true)
    @NotNull
    @Size(min = 1, message = "items cannot be empty")
    @Valid
    private List<CheckedOutBook> items;
}
