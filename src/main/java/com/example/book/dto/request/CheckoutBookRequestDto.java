package com.example.book.dto.request;

import com.example.book.model.CheckedOutBook;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class CheckoutBookRequestDto {

    private String promoCode;

    @NotNull
    @Size(min = 1, message = "items cannot be empty")
    @Valid
    private List<CheckedOutBook> items;
}
