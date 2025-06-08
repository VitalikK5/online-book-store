package com.example.bookhub.mapper;

import com.example.bookhub.config.MapperConfig;
import com.example.bookhub.dto.BookDto;
import com.example.bookhub.dto.CreateBookRequestDto;
import com.example.bookhub.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
