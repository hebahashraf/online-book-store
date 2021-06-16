package com.example.book.controller;

import com.example.book.BookHelper;
import com.example.book.dto.BookDto;
import com.example.book.dto.request.BookRequestDto;
import com.example.book.dto.request.CheckoutBookRequestDto;
import com.example.book.dto.response.CheckoutBookResponse;
import com.example.book.dto.response.CreateBookResponseDto;
import com.example.book.model.CheckedOutBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerITTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void flowTest() {

        // get all books
        HttpEntity<Void> getAllHttpEntity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<List<BookDto>> getBooksResponse = testRestTemplate.exchange(
                "/books",
                HttpMethod.GET,
                getAllHttpEntity,
                new ParameterizedTypeReference<>() {});
        assertEquals(HttpStatus.OK, getBooksResponse.getStatusCode());
        assertEquals(0, getBooksResponse.getBody().size());

        // create book
        BookRequestDto createRequest = BookHelper.getBookRequestDto();
        HttpEntity<BookRequestDto> createHttpEntity = new HttpEntity<>(createRequest);
        ResponseEntity<CreateBookResponseDto> createResponse = testRestTemplate.exchange(
                "/books",
                HttpMethod.POST,
                createHttpEntity,
                CreateBookResponseDto.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        // get all books
        getBooksResponse = testRestTemplate.exchange(
                "/books",
                HttpMethod.GET,
                getAllHttpEntity,
                new ParameterizedTypeReference<>() {});
        assertEquals(HttpStatus.OK, getBooksResponse.getStatusCode());
        assertEquals(1, getBooksResponse.getBody().size());

        final String bookId = createResponse.getBody().getId();

        // get book details
        HttpEntity<Void> getBookHttpEntity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<BookDto> getBookResponse = testRestTemplate.exchange(
                "/books/" + bookId,
                HttpMethod.GET,
                getBookHttpEntity,
                BookDto.class);
        assertEquals(HttpStatus.OK, getBookResponse.getStatusCode());

        // update book details
        BookRequestDto updateRequest = BookHelper.getBookRequestDto();
        updateRequest.setBookPrice(BigDecimal.valueOf(50));
        HttpEntity<BookRequestDto> updateHttpEntity = new HttpEntity<>(updateRequest);
        ResponseEntity<BookDto> updateResponse = testRestTemplate.exchange(
                "/books/" + bookId,
                HttpMethod.PUT,
                updateHttpEntity,
                BookDto.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(BigDecimal.valueOf(50), updateResponse.getBody().getBookPrice());

        BookDto bookDto = updateResponse.getBody();
        CheckoutBookRequestDto checkoutRequest = new CheckoutBookRequestDto();
        checkoutRequest.setPromoCode("code1");
        CheckedOutBook item = new CheckedOutBook();
        item.setType(bookDto.getType());
        item.setQuantity(10);
        item.setId(bookDto.getId());
        item.setIsbn(bookDto.getIsbn());
        item.setBookName(bookDto.getBookName());
        item.setBookPrice(bookDto.getBookPrice());
        checkoutRequest.setItems(List.of(item));

        HttpEntity<CheckoutBookRequestDto> checkoutEntity = new HttpEntity<>(checkoutRequest);

        ResponseEntity<CheckoutBookResponse> checkoutResponse = testRestTemplate.exchange(
                "/books/checkout",
                HttpMethod.POST,
                checkoutEntity,
                CheckoutBookResponse.class);
        assertEquals(HttpStatus.OK, checkoutResponse.getStatusCode());

        // delete book details
        HttpEntity<Void> deleteHttpEntity = new HttpEntity<>(new HttpHeaders());
        ResponseEntity<Void> deleteResponse = testRestTemplate.exchange(
                "/books/" + bookId,
                HttpMethod.DELETE,
                deleteHttpEntity,
                Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // get all books
        getBooksResponse = testRestTemplate.exchange(
                "/books",
                HttpMethod.GET,
                getAllHttpEntity,
                new ParameterizedTypeReference<>() {});
        assertEquals(HttpStatus.OK, getBooksResponse.getStatusCode());
        assertEquals(0, getBooksResponse.getBody().size());
    }
}
