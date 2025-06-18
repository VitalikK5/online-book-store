package com.example.bookhub.mapper;

import com.example.bookhub.config.MapperConfig;
import com.example.bookhub.dto.BookDto;
import com.example.bookhub.dto.CreateBookRequestDto;
import com.example.bookhub.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "deleted", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "deleted", ignore = true)
    void updateFromDto(@MappingTarget Book book, CreateBookRequestDto requestDto);
}
