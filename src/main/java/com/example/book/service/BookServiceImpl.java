package com.example.book.service;

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
import com.example.book.model.DiscountRule;
import com.example.book.model.PromotionRule;
import com.example.book.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

	private static final String BOOK_UNAVAILABLE_MSG = "Requested book is not available in the store!!!";

	private final BookRepository repository;
	private final BookMapper bookMapper;
	private final DiscountConfig discountConfig;

	public BookServiceImpl(BookRepository repository, BookMapper bookMapper, DiscountConfig discountConfig) {
		this.repository = repository;
		this.bookMapper = bookMapper;
		this.discountConfig = discountConfig;
	}

	@Override
	public CreateBookResponseDto saveBook(BookRequestDto request) {
		Book book = repository.save(bookMapper.fromBookRequestDto(request));
		return new CreateBookResponseDto(book.getId().toString());
	}

	@Override
	public List<BookDto> getBooks() {
		return repository.findByStatus(BookStatus.ACTIVE).stream()
				.map(bookMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public BookDto getBook(Long id) {
		return repository.findById(id)
				.map(book -> {
					if (book.getStatus() == BookStatus.DELETED)
						throw new BusinessException(HttpStatus.NOT_FOUND, "This book is no longer sold here");
					return bookMapper.toDto(book);
				}).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, BOOK_UNAVAILABLE_MSG));
	}

	@Override
	public void deleteBook(Long id) {
		repository.findById(id)
				.map(book -> {
					if (book.getStatus() == BookStatus.DELETED)
						throw new BusinessException(HttpStatus.CONFLICT, "This book is no longer sold here");
					book.setStatus(BookStatus.DELETED);
					repository.save(book);
					return book;
				}).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, BOOK_UNAVAILABLE_MSG));
	}

	@Override
	public BookDto updateBook(Long id, BookRequestDto request) {
		return repository.findById(id)
				.map(book -> {
					if (book.getStatus() == BookStatus.DELETED) {
						throw new BusinessException(HttpStatus.CONFLICT, "This book is no longer sold here. Cannot update it");
					}
					Book updatedBook = bookMapper.fromBookRequestDto(request);
					updatedBook.setId(id);
					repository.save(updatedBook);
					return bookMapper.toDto(updatedBook);
				}).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, BOOK_UNAVAILABLE_MSG));
	}

	@Override
	public CheckoutBookResponse calculateBookPayment(CheckoutBookRequestDto request) {

		List<DiscountRule> discountRules = new ArrayList<>();

		if (request.getPromoCode() != null) {
			discountRules = discountConfig.getPromotionRules().stream()
					.filter(x -> x.getPromotionCode().equals(request.getPromoCode()))
					.map(PromotionRule::getDiscountRules)
					.findFirst()
					.orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Invalid promo code"));
		}

		List<CheckedOutBook> items = request.getItems();

		BigDecimal originalPrice = withScaleTwo(
				items.stream().map(x -> x.getBookPrice().multiply(BigDecimal.valueOf(x.getQuantity())))
						.reduce(BigDecimal.ZERO, BigDecimal::add));

		BigDecimal priceAfterDiscount = BigDecimal.ZERO;

		for (CheckedOutBook item : items) {
			BigDecimal originalAmount = item.getBookPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
			BigDecimal discountForItem = BigDecimal.ZERO;
			for (DiscountRule discountRule : discountRules) {
				if (discountRule.getType().equals(item.getType())) {
					discountForItem = getDiscountValue(originalAmount, discountRule.getDiscount());
					break;
				}
			}
			priceAfterDiscount = withScaleTwo(
					priceAfterDiscount.add(originalAmount.subtract(discountForItem))
			);
		}

		return new CheckoutBookResponse(originalPrice, priceAfterDiscount);

	}

	private static BigDecimal getDiscountValue(BigDecimal amount, BigDecimal discount) {
		return amount.multiply(discount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
	}

	private static BigDecimal withScaleTwo(BigDecimal amount) {
		return amount.setScale(2, RoundingMode.HALF_EVEN);
	}

}

