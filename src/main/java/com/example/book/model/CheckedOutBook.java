package com.example.book.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Quantity of the book to be checked out", required = true, example = "2")
    @NotNull(message = "quantity cannot be null")
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

    @Schema(description = "Name of the book", example = "Harry Potter")
    @Pattern(regexp = "^[A-Za-z0-9\\s\\-_,\\.;:()]+$", message = "bookName invalid")
    private String bookName;

    @Schema(description = "description of the book", example = "A sci-fi mystery thriller")
    private String bookDescription;

    @Schema(description = "Author of the book", example = "JK Rowling")
    private String author;

    @Schema(description = "type of the book. Discount varies based on the type. For example, for a valid promo code," +
            " FICTION type books might have 10% but COMICS might have only 5% discount", example = "FICTION")
    private String type;

    @Schema(description = "id of the book.", example = "1")
    private String id;

    @Schema(description = "Cost of the book.", required = true, example = "100")
    @NotNull(message = "bookPrice cannot be null")
    @DecimalMin(value = "0.01", message = "bookPrice must be greater than 0")
    private BigDecimal bookPrice;

    @Schema(description = "ISBN (International Standard Book Number) of the book.", example = "1231231232122")
    @Pattern(regexp = "\\d{13}", message = "isbn must be exactly 13 digits")
    private String isbn;
}
