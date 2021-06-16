package com.example.book;

import com.example.book.dto.BookDto;
import com.example.book.dto.request.BookRequestDto;
import com.example.book.entity.Book;
import com.example.book.model.BookStatus;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class BookHelper {

    public Book getBookWithoutId() {
        return Book.builder()
                .bookName("harry potter")
                .bookPrice(BigDecimal.valueOf(100))
                .bookDescription("Best selling sci-fi book")
                .author("JK Rowling")
                .isbn("1234567890123")
                .build();
    }

    public Book getBookWithId() {
        return Book.builder()
                .bookName("harry potter")
                .bookPrice(BigDecimal.valueOf(100))
                .bookDescription("Best selling sci-fi book")
                .author("JK Rowling")
                .isbn("1234567890123")
                .id(1L)
                .status(BookStatus.ACTIVE)
                .type("COMICS")
                .build();
    }

    public BookDto getBookDtoWithoutId() {
        return BookDto.builder()
                .bookName("harry potter")
                .bookPrice(BigDecimal.valueOf(100))
                .bookDescription("Best selling sci-fi book")
                .author("JK Rowling")
                .isbn("1234567890123")
                .build();
    }

    public BookRequestDto getBookRequestDto() {
        return BookRequestDto.builder()
                .bookName("harry potter")
                .bookPrice(BigDecimal.valueOf(100))
                .bookDescription("Best selling sci-fi book")
                .author("JK Rowling")
                .isbn("1234567890123")
                .type("COMICS")
                .build();
    }

    public BookDto getBookDto() {
        return BookDto.builder()
                .bookName("harry potter")
                .bookPrice(BigDecimal.valueOf(100))
                .bookDescription("Best selling sci-fi book")
                .author("JK Rowling")
                .isbn("1234567890123")
                .type("COMICS")
                .id("1")
                .build();
    }

    public List<Book> getBooks() {

        Book book1 = Book.builder()
                .bookName("harry potter")
                .bookPrice(BigDecimal.valueOf(100))
                .bookDescription("Best selling sci-fi book")
                .author("JK Rowling")
                .isbn("1234567890123")
                .id(1L)
                .build();

        Book book2 = Book.builder()
                .bookName("harry potter")
                .bookPrice(BigDecimal.valueOf(100))
                .bookDescription("Best selling sci-fi book")
                .author("JK Rowling")
                .isbn("1234567890123")
                .id(2L)
                .build();

        return List.of(book1, book2);
    }

    public List<BookDto> getBooksDto() {

        BookDto book1 = BookDto.builder()
                .bookName("harry potter")
                .bookPrice(BigDecimal.valueOf(100))
                .bookDescription("Best selling sci-fi book")
                .author("JK Rowling")
                .isbn("1234567890123")
                .id("1")
                .build();

        BookDto book2 = BookDto.builder()
                .bookName("harry potter")
                .bookPrice(BigDecimal.valueOf(100))
                .bookDescription("Best selling sci-fi book")
                .author("JK Rowling")
                .isbn("1234567890123")
                .id("2")
                .build();

        return List.of(book1, book2);
    }
}
