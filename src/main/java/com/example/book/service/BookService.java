package com.example.book.service;

import com.example.book.dto.BookDto;
import com.example.book.dto.request.BookRequestDto;
import com.example.book.dto.request.CheckoutBookRequestDto;
import com.example.book.dto.response.CheckoutBookResponse;
import com.example.book.dto.response.CreateBookResponseDto;

import java.util.List;

public interface BookService {

    CreateBookResponseDto saveBook(BookRequestDto book);

    List<BookDto> getBooks();

    BookDto getBook(Long id);

    void deleteBook(Long id);

    BookDto updateBook(Long id, BookRequestDto request);

    CheckoutBookResponse calculateBookPayment(CheckoutBookRequestDto request);
}
