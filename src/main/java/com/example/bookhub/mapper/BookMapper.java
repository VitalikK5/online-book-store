package com.example.bookhub.mapper;

import com.example.bookhub.config.MapperConfig;
import com.example.bookhub.dto.book.BookDto;
import com.example.bookhub.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookhub.dto.book.CreateBookRequestDto;
import com.example.bookhub.model.Book;
import com.example.bookhub.model.Category;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "deleted", ignore = true)
    Book toEntity(CreateBookRequestDto requestDto);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "deleted", ignore = true)
    void updateFromDto(@MappingTarget Book book, CreateBookRequestDto requestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            List<Long> categoryIds = book.getCategories()
                    .stream()
                    .map(Category::getId)
                    .toList();
            bookDto.setCategoryIds(categoryIds);
        }
    }
}
