package com.example.book.mapper;

import com.example.book.dto.request.BookRequestDto;
import com.example.book.dto.BookDto;
import com.example.book.entity.Book;
import com.example.book.model.BookStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", imports = {BookStatus.class})
public interface BookMapper {

    BookDto toDto(Book book);

    @Mappings(
            @Mapping(target = "status", expression = "java(BookStatus.ACTIVE)")
    )
    Book fromBookRequestDto(BookRequestDto bookRequestDto);
}
