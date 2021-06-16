package com.example.book.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    @Schema(description = "unique id of the book", required = true, example = "1")
    @NotNull
    private String id;

    @Schema(description = "name of the book", required = true, example = "Harry Potter")
    @NotNull
    private String bookName;

    @Schema(description = "description of the book", example = "A sci-fi thriller")
    private String bookDescription;

    @Schema(description = "Author of the book", required = true, example = "JK Rowling")
    @NotNull
    private String author;

    @Schema(description = "type of the book. Example - FICTION, COMICS etc", required = true, example = "FICTION")
    @NotNull
    private String type;

    @Schema(description = "Cost of the book", required = true, example = "20.04")
    @NotNull
    private BigDecimal bookPrice;

    @Schema(description = "ISBN (International Standard Book Number) of the book.", required = true, example = "1231231232122")
    @NotNull
    private String isbn;
}
