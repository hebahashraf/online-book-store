package com.example.book.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private String id;
    private String bookName;
    private String bookDescription;
    private String author;
    private String type;
    private BigDecimal bookPrice;
    private String isbn;
}
