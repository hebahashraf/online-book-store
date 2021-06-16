package com.example.book.controller;

import com.example.book.dto.BookDto;
import com.example.book.dto.request.BookRequestDto;
import com.example.book.dto.request.CheckoutBookRequestDto;
import com.example.book.dto.response.CheckoutBookResponse;
import com.example.book.dto.response.CreateBookResponseDto;
import com.example.book.service.BookService;
import com.example.book.util.BookUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Add new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Added book successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreateBookResponseDto.class)) }),
            @ApiResponse(responseCode = "400", description = "bad request",
                    content = @Content)})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Add book request",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookRequestDto.class)) })
    @PostMapping(value = "/books", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateBookResponseDto> addBook(@RequestBody @Valid BookRequestDto book) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.saveBook(book));
    }

    @Operation(summary = "Get all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retrieved all books successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class)) })
    })
    @GetMapping(value = "/books", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookDto>> findAllBooks() {
        return ResponseEntity.ok(bookService.getBooks());
    }

    @Operation(summary = "Get a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content) })
    @GetMapping(value = "/books/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> findBookByID(
            @Parameter(description="Id of the book. Must be a positive integer", required=true) @PathVariable String id) {
        return ResponseEntity.ok(bookService.getBook(BookUtils.parse(id)));
    }


    @Operation(summary = "Update a book with the given Id")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Update book request",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookRequestDto.class)) })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the book successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Book cannot be updated as it's already deleted",
                    content = @Content)  })
    @PutMapping(value = "/books/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDto> updateBook(
            @Parameter(description="Id of the book. Must be a positive integer", required=true) @PathVariable String id,
            @RequestBody @Valid BookRequestDto book) {
        return ResponseEntity.ok(bookService.updateBook(BookUtils.parse(id), book));
    }

    @Operation(summary = "Delete a book with the given Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Deleted the book successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Book already deleted",
                    content = @Content)  })
    @DeleteMapping(value = "/books/{id}")
    public ResponseEntity<Void> deleteBook(
            @Parameter(description="Id of the book. Must be a positive integer", required=true) @PathVariable String id) {
        bookService.deleteBook(BookUtils.parse(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Checkout books and display the total price after discount if applicable")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Checkout Books request",
            content = { @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CheckoutBookRequestDto.class)) })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checkout successful",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CheckoutBookResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid promo code",
                    content = @Content) })
    @PostMapping("/books/checkout")
    public CheckoutBookResponse checkOut(@RequestBody @Valid CheckoutBookRequestDto request) {
        return bookService.calculateBookPayment(request);
    }
}
