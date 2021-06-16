package com.example.book.repository;

import com.example.book.entity.Book;
import com.example.book.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByStatus(BookStatus status);
}
