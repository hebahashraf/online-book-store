package com.example.book.controller;

import com.example.book.BookHelper;
import com.example.book.context.AppContext;
import com.example.book.advice.BooksAdvice;
import com.example.book.dto.BookDto;
import com.example.book.dto.request.BookRequestDto;
import com.example.book.dto.request.CheckoutBookRequestDto;
import com.example.book.dto.response.CheckoutBookResponse;
import com.example.book.dto.response.CreateBookResponseDto;
import com.example.book.model.CheckedOutBook;
import com.example.book.service.BookService;
import com.example.book.util.JsonTool;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BooksControllerTest {

    @Mock
    private BookService bookService;

    private MockMvc mockMvc;
    private final JsonTool jsonTool = JsonTool.custom(AppContext.objectMapper());

    @BeforeEach
    void before() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new BooksController(bookService))
                .setMessageConverters(new MappingJackson2HttpMessageConverter(AppContext.objectMapper()))
                .setControllerAdvice(new BooksAdvice())
                .build();
    }

    @Test
    void addBook() throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        CreateBookResponseDto expectedResponse = new CreateBookResponseDto("1");

        when(bookService.saveBook(isA(BookRequestDto.class)))
                .thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/books")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CreateBookResponseDto actualResponse = jsonTool.load(
                result.getResponse().getContentAsString(), CreateBookResponseDto.class);

        assertEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void addBook_MissingAuthor(String author) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setAuthor(author);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/books")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("msg : author cannot be blank")));

    }

    @Test
    void addBook_MissingBookPrice() throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setBookPrice(null);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/books")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("field: 'bookPrice', rejected val: [null]")));

    }

    @ParameterizedTest()
    @ValueSource(doubles = {-0.01, 0.00})
    void addBook_InvalidBookPrice(double bookPrice) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setBookPrice(BigDecimal.valueOf(bookPrice));
        mockMvc.perform(
                MockMvcRequestBuilders.post("/books")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("msg : bookPrice must be greater than 0")));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"bå§..a", "âsda¨¨"})
    void addBook_InvalidBookName(String bookName) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setBookName(bookName);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/books")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("bookName invalid")));
    }

    @ParameterizedTest()
    @NullSource
    @EmptySource
    void addBook_WithoutBookName(String bookName) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setBookName(bookName);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/books")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("bookName cannot be blank")));
    }

    @ParameterizedTest
    @EmptySource
    void addBook_MissingType(String type) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setType(type);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/books")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("type cannot be blank")));
    }

    @Test
    void addBook_MissingISBN() throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setIsbn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/books")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("field: 'isbn', rejected val: [null]")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123123", "12321231221111"})
    void addBook_InvalidISBN(String isbn) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setIsbn(isbn);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/books")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("msg : isbn must be exactly 13 digits")));
    }

    @Test
    void getAllBooks() throws Exception {

        List<BookDto> expectedResponse = BookHelper.getBooksDto();
        when(bookService.getBooks()).thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(jsonTool.toJson(expectedResponse), result.getResponse().getContentAsString());
    }

    @Test
    void getBook() throws Exception {

        BookDto expectedResponse = BookHelper.getBookDto();
        when(bookService.getBook(1L)).thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/books/1"))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualResponse = jsonTool.load(result.getResponse().getContentAsString(), BookDto.class);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getBook_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/1a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        StringContains.containsString("Book Id format not valid")));
    }

    @Test
    void updateBook() throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();

        BookDto expectedResponse = BookHelper.getBookDto();
        expectedResponse.setBookDescription("Best selling sci-fi book");

        when(bookService.updateBook(eq(1L), isA(BookRequestDto.class))).thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/books/1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonTool.toJson(request)))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualResponse = jsonTool.load(result.getResponse().getContentAsString(), BookDto.class);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void updateBook_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/books/1a")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonTool.toJson(BookHelper.getBookRequestDto())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        StringContains.containsString("Book Id format not valid")));
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    void updateBook_MissingAuthor(String author) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setAuthor(author);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/1")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("msg : author cannot be blank")));

    }

    @Test
    void updateBook_MissingBookPrice() throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setBookPrice(null);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/1")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("field: 'bookPrice', rejected val: [null]")));

    }

    @ParameterizedTest()
    @ValueSource(doubles = {-0.01, 0.00})
    void updateBook_InvalidBookPrice(double bookPrice) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setBookPrice(BigDecimal.valueOf(bookPrice));
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/1")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("msg : bookPrice must be greater than 0")));
    }

    @ParameterizedTest()
    @ValueSource(strings = {"bå§..a", "âsda¨¨"})
    void updateBook_InvalidBookName(String bookName) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setBookName(bookName);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/1")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("bookName invalid")));
    }

    @ParameterizedTest()
    @NullSource
    @EmptySource
    void updateBook_WithoutBookName(String bookName) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setBookName(bookName);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/1")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("bookName cannot be blank")));
    }

    @ParameterizedTest
    @EmptySource
    void updateBook_MissingType(String type) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setType(type);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/1")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("type cannot be blank")));
    }

    @Test
    void updateBook_MissingISBN() throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setIsbn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/1")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("field: 'isbn', rejected val: [null]")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"123123", "12321231221111"})
    void updateBook_InvalidISBN(String isbn) throws Exception {

        BookRequestDto request = BookHelper.getBookRequestDto();
        request.setIsbn(isbn);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/1")
                        .content(jsonTool.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(StringContains.containsString("msg : isbn must be exactly 13 digits")));
    }

    @Test
    void deleteBook() throws Exception {
        doNothing().when(bookService).deleteBook(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBook_InvalidId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/1a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        StringContains.containsString("Book Id format not valid")));
    }

    @Test
    void checkout() throws Exception {
        CheckoutBookRequestDto request = new CheckoutBookRequestDto();
        request.setPromoCode("code3");

        CheckedOutBook checkedOutBook = new CheckedOutBook();
        checkedOutBook.setType("FICTION");
        checkedOutBook.setQuantity(10);
        checkedOutBook.setBookPrice(BigDecimal.valueOf(100));
        request.setItems(List.of(checkedOutBook));

        CheckoutBookResponse expectedResponse = new CheckoutBookResponse();
        expectedResponse.setOriginalPrice(BigDecimal.valueOf(1000));
        expectedResponse.setPriceAfterDiscount(BigDecimal.valueOf(1000));

        when(bookService.calculateBookPayment(isA(CheckoutBookRequestDto.class))).thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/books/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTool.toJson(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(expectedResponse, jsonTool.load(
                result.getResponse().getContentAsString(), CheckoutBookResponse.class));
    }

    @Test
    void checkout_MissingItems() throws Exception {
        CheckoutBookRequestDto request = new CheckoutBookRequestDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/books/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTool.toJson(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        StringContains.containsString("field: 'items', rejected val: [null]")));
    }

}
