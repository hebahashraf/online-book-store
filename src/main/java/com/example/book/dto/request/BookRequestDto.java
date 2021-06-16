package com.example.book.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDto {

    @Schema(description = "name of the book", required = true, example = "Harry Potter")
    @NotBlank(message = "bookName cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9\\s\\-_,\\.;:()]+$", message = "bookName invalid")
    private String bookName;

    @Schema(description = "description of the book", example = "A sci-fi thriller")
    private String bookDescription;

    @Schema(description = "Author of the book", required = true, example = "JK Rowling")
    @NotBlank(message = "author cannot be blank")
    private String author;

    @Schema(description = "type of the book. Example - FICTION, COMICS etc", required = true, example = "FICTION")
    @NotBlank(message = "type cannot be blank")
    private String type;

    @Schema(description = "Cost of the book", required = true, example = "20.04")
    @NotNull(message = "bookPrice cannot be null")
    @DecimalMin(value = "0.01", message = "bookPrice must be greater than 0")
    private BigDecimal bookPrice;

    @NotNull
    @Schema(description = "ISBN (International Standard Book Number) of the book.", required = true, example = "1231231232122")
    @Pattern(regexp = "\\d{13}", message = "isbn must be exactly 13 digits")
    private String isbn;
}
