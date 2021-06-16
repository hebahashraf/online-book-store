package com.example.book.controller;

import com.example.book.dto.BookDto;
import com.example.book.dto.request.BookRequestDto;
import com.example.book.dto.request.CheckoutBookRequestDto;
import com.example.book.dto.response.CheckoutBookResponse;
import com.example.book.dto.response.CreateBookResponseDto;
import com.example.book.service.BookService;
import com.example.book.util.BookUtils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class BooksController {

	private final BookService bookService;

	public BooksController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping(value = "/books", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<CreateBookResponseDto> addBook(@RequestBody @Valid BookRequestDto book) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookService.saveBook(book));
	}

	@GetMapping(value = "/books", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<List<BookDto>> findAllBooks() {
		return ResponseEntity.ok(bookService.getBooks());
	}

	@GetMapping(value = "/books/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<BookDto> findBookByID(@PathVariable String id) {
		return ResponseEntity.ok(bookService.getBook(BookUtils.parse(id)));
	}

	@PutMapping(value = "/books/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity<BookDto> updateBook(@PathVariable("id") String id, @RequestBody @Valid BookRequestDto book) {
		return ResponseEntity.ok(bookService.updateBook(BookUtils.parse(id), book));
	}

	@DeleteMapping(value = "/books/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable String id) {
		bookService.deleteBook(BookUtils.parse(id));
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/books/checkout")
	public CheckoutBookResponse checkOut(@RequestBody @Valid CheckoutBookRequestDto request) {
		return bookService.calculateBookPayment(request);
	}
}
