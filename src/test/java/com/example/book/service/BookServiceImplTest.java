package com.example.book.service;

import com.example.book.BookHelper;
import com.example.book.config.DiscountConfig;
import com.example.book.dto.BookDto;
import com.example.book.dto.request.BookRequestDto;
import com.example.book.dto.request.CheckoutBookRequestDto;
import com.example.book.dto.response.CheckoutBookResponse;
import com.example.book.dto.response.CreateBookResponseDto;
import com.example.book.entity.Book;
import com.example.book.exception.BusinessException;
import com.example.book.mapper.BookMapper;
import com.example.book.model.BookStatus;
import com.example.book.model.CheckedOutBook;
import com.example.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookServiceImplTest {

    private BookService bookService;
    @MockBean
    private BookRepository repository;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private DiscountConfig discountConfig;


    @BeforeEach
    void before() {
        bookService = new BookServiceImpl(repository, bookMapper, discountConfig);
    }

    @Test
    void saveBook() {
        BookRequestDto request = BookHelper.getBookRequestDto();
        Book book = BookHelper.getBookWithId();
        when(repository.save(isA(Book.class))).thenReturn(book);
        assertEquals(new CreateBookResponseDto("1"), bookService.saveBook(request));
    }

    @Test
    void getBooks() {
        List<BookDto> expectedResponse = BookHelper.getBooksDto();
        List<Book> books = BookHelper.getBooks();
        when(repository.findByStatus(isA(BookStatus.class))).thenReturn(books);
        assertEquals(expectedResponse, bookService.getBooks());
    }

    @Test
    void getBookbyId() {
        Book book = BookHelper.getBookWithId();
        BookDto expectedResponse = BookHelper.getBookDto();
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        assertEquals(expectedResponse, bookService.getBook(1L));
    }

    @Test
    void getBookbyId_NotFound() {
        Book book = BookHelper.getBookWithId();
        BookDto expectedResponse = BookHelper.getBookDto();
        when(repository.findById(1L)).thenReturn(Optional.empty());
        BusinessException ex = assertThrows(BusinessException.class, () -> bookService.getBook(1L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals("Requested book is not available in the store!!!", ex.getMessage());
    }

    @Test
    void deleteBook() {
        Book book = BookHelper.getBookWithId();
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        bookService.deleteBook(1L);
    }

    @Test
    void deleteBook_NotFound() {
        final String errorMsg = "Requested book is not available in the store!!!";

        when(repository.findById(1L))
                .thenThrow(new BusinessException(HttpStatus.NOT_FOUND, errorMsg));

        BusinessException ex = assertThrows(BusinessException.class, () -> bookService.deleteBook(1L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals(errorMsg, ex.getMessage());
    }

    @Test
    void deleteBook_AlreadyDeleted() {
        final String errorMsg = "This book is no longer sold here";

        Book book = BookHelper.getBookWithId();
        book.setStatus(BookStatus.DELETED);

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        BusinessException ex = assertThrows(BusinessException.class, () -> bookService.deleteBook(1L));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals(errorMsg, ex.getMessage());
    }

    @Test
    void updateBook() {
        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setAuthor("JK Rowling!");

        BookDto expectedResponse = BookHelper.getBookDto();
        expectedResponse.setAuthor("JK Rowling!");

        Book book = BookHelper.getBookWithId();

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        assertEquals(expectedResponse, bookService.updateBook(1L, request));
    }

    @Test
    void updateBook_NotFound() {
        BookRequestDto request = BookHelper.getBookRequestDto();
        final String errorMsg = "Requested book is not available in the store!!!";

        when(repository.findById(1L))
                .thenThrow(new BusinessException(HttpStatus.NOT_FOUND, errorMsg));

        BusinessException ex = assertThrows(BusinessException.class, () -> bookService.updateBook(1L, request));
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
        assertEquals(errorMsg, ex.getMessage());
    }

    @Test
    void checkout() {
        CheckoutBookRequestDto requestDto = new CheckoutBookRequestDto();
        requestDto.setPromoCode("code1");

        CheckedOutBook checkedOutBook = new CheckedOutBook();
        checkedOutBook.setType("FICTION");
        checkedOutBook.setQuantity(10);
        checkedOutBook.setBookPrice(BigDecimal.valueOf(100));

        requestDto.setItems(Collections.singletonList(checkedOutBook));

        CheckoutBookResponse response = bookService.calculateBookPayment(requestDto);

        // 100 * 10 = 1000
        assertEquals(getBigDecimal(1000), response.getOriginalPrice());
        // 1000 - (100 * 10) / 100
        assertEquals(getBigDecimal(900), response.getPriceAfterDiscount());
    }

    @Test
    void checkout_MultipleItems() {
        CheckoutBookRequestDto requestDto = new CheckoutBookRequestDto();
        requestDto.setPromoCode("code1");

        // FICTION has 10 percent discount
        CheckedOutBook checkedOutBook1 = new CheckedOutBook();
        checkedOutBook1.setType("FICTION");
        checkedOutBook1.setQuantity(10);
        checkedOutBook1.setBookPrice(BigDecimal.valueOf(100));

        // COMICS has 5% discount
        CheckedOutBook checkedOutBook2 = new CheckedOutBook();
        checkedOutBook2.setType("COMICS");
        checkedOutBook2.setQuantity(5);
        checkedOutBook2.setBookPrice(BigDecimal.valueOf(10));

        // thriller does not have any discount
        CheckedOutBook checkedOutBook3 = new CheckedOutBook();
        checkedOutBook3.setType("THRILLER");
        checkedOutBook3.setQuantity(3);
        checkedOutBook3.setBookPrice(BigDecimal.valueOf(1));

        requestDto.setItems(List.of(checkedOutBook1, checkedOutBook2, checkedOutBook3));

        // 100*10 + 5*10 + 3*1 = 1000 + 50 + 3 = 1053
        final BigDecimal expectedOriginalPrice = getBigDecimal(1053);
        // 10% of 1000 + 5% of 50 + 3 = 950.50
        final BigDecimal expectedPriceAfterDiscount = getBigDecimal(950.50);

        CheckoutBookResponse response = bookService.calculateBookPayment(requestDto);

        assertEquals(expectedOriginalPrice, response.getOriginalPrice());
        assertEquals(expectedPriceAfterDiscount, response.getPriceAfterDiscount());
    }

    @Test
    void checkout_NoPromoCode() {
        CheckoutBookRequestDto requestDto = new CheckoutBookRequestDto();

        CheckedOutBook checkedOutBook = new CheckedOutBook();
        checkedOutBook.setType("FICTION");
        checkedOutBook.setQuantity(10);
        checkedOutBook.setBookPrice(BigDecimal.valueOf(100));

        requestDto.setItems(Collections.singletonList(checkedOutBook));

        CheckoutBookResponse response = bookService.calculateBookPayment(requestDto);

        assertEquals(getBigDecimal(1000), response.getOriginalPrice());
        assertEquals(getBigDecimal(1000), response.getPriceAfterDiscount());
    }

    private static BigDecimal getBigDecimal(double amount) {
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_EVEN);
    }

    @Test
    void checkout_InvalidPromoCode() {
        CheckoutBookRequestDto requestDto = new CheckoutBookRequestDto();
        requestDto.setPromoCode("code3");

        CheckedOutBook checkedOutBook = new CheckedOutBook();
        checkedOutBook.setType("FICTION");
        checkedOutBook.setQuantity(10);
        checkedOutBook.setBookPrice(BigDecimal.valueOf(100));

        requestDto.setItems(Collections.singletonList(checkedOutBook));

        BusinessException ex = assertThrows(BusinessException.class, () -> bookService.calculateBookPayment(requestDto));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertEquals("Invalid promo code", ex.getMessage());
    }
}