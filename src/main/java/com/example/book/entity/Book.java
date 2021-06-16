package com.example.book.entity;

import com.example.book.model.BookStatus;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TBL_BOOK")
@Getter
@Setter
@Builder
public class Book extends Auditable {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "book_name", nullable = false)
	private String bookName;

	private String bookDescription;

	@Column(name = "author", nullable = false)
	private String author;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "book_price", nullable = false)
	private BigDecimal bookPrice;

	@Column(name = "isbn", nullable = false, unique = true)
	private String isbn;

	@Enumerated(EnumType.STRING)
	private BookStatus status;
}
