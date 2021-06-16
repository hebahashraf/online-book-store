package com.example.book.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckedOutBook {

    @NotNull(message = "quantity cannot be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @Pattern(regexp = "^[A-Za-z0-9\\s\\-_,\\.;:()]+$", message = "bookName invalid")
    private String bookName;

    private String bookDescription;

    private String author;

    private String type;

    private String id;

    @NotNull(message = "bookPrice cannot be null")
    @DecimalMin(value = "0.01", message = "bookPrice must be greater than 0")
    private BigDecimal bookPrice;

    @Pattern(regexp = "\\d{13}", message = "isbn must be exactly 13 digits")
    private String isbn;
}
