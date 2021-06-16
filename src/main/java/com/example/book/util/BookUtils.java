package com.example.book.util;

import com.example.book.exception.BusinessException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class BookUtils {

    public Long parse(String bookId) {
        try {
            return Long.valueOf(bookId);
        } catch (NumberFormatException ex) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Book Id format not valid");
        }
    }

}
