package com.example.book.dto.request;

import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDto {

    @NotBlank(message = "bookName cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9\\s\\-_,\\.;:()]+$", message = "bookName invalid")
    private String bookName;

    private String bookDescription;

    @NotBlank(message = "author cannot be blank")
    private String author;

    @NotBlank(message = "type cannot be blank")
    private String type;

    @NotNull(message = "bookPrice cannot be null")
    @DecimalMin(value = "0.01", message = "bookPrice must be greater than 0")
    private BigDecimal bookPrice;

    @NotNull
    @Pattern(regexp = "\\d{13}", message = "isbn must be exactly 13 digits")
    private String isbn;
}
