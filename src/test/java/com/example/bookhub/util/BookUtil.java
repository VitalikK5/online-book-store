package com.example.bookhub.util;

import com.example.bookhub.dto.book.BookDto;
import com.example.bookhub.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookhub.dto.book.CreateBookRequestDto;
import com.example.bookhub.model.Book;
import com.example.bookhub.model.Category;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookUtil {
    public static CreateBookRequestDto createBookRequestDto(List<Long> categoryIds) {
        return new CreateBookRequestDto()
                .setTitle("Test Book 1")
                .setAuthor("Test Author 1")
                .setIsbn("1111111111112")
                .setPrice(BigDecimal.valueOf(15.99))
                .setDescription("Test description")
                .setCoverImage("http://example.com/test-cover.jpg")
                .setCategoryIds(categoryIds);
    }

    public static Book createBook(Long bookId, Set<Category> categories) {
        return new Book()
                .setId(bookId)
                .setTitle("Test Book 1")
                .setAuthor("Test Author 1")
                .setIsbn("1111111111112")
                .setPrice(BigDecimal.valueOf(15.99))
                .setDescription("Test description")
                .setCoverImage("http://example.com/test-cover.jpg")
                .setCategories(categories != null ? categories : new HashSet<>());
    }

    public static BookDto createBookDto(Long id, List<Long> categoryIds) {
        return new BookDto()
                .setId(id)
                .setTitle("Test Book 1")
                .setAuthor("Test Author 1")
                .setIsbn("1111111111112")
                .setPrice(BigDecimal.valueOf(15.99))
                .setDescription("Test description")
                .setCoverImage("http://example.com/test-cover.jpg")
                .setCategoryIds(categoryIds);
    }

    public static BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds(Long id) {
        return new BookDtoWithoutCategoryIds()
                .setId(id)
                .setTitle("Test Book 1")
                .setAuthor("Test Author 1")
                .setIsbn("1111111111112")
                .setPrice(BigDecimal.valueOf(15.99))
                .setDescription("Test description")
                .setCoverImage("http://example.com/test-cover.jpg");
    }

    public static Category createCategory(Long id, String name) {
        return new Category()
                .setId(id)
                .setName(name);
    }

    public static List<BookDto> createListOfBookDtos() {
        BookDto firstDto = createBookDto(1L, List.of(1L))
                .setTitle("Test Book 1")
                .setAuthor("Test Author 1")
                .setIsbn("1111111111111")
                .setPrice(BigDecimal.valueOf(10.99));

        BookDto secondDto = createBookDto(2L, List.of(1L))
                .setTitle("Test Book 2")
                .setAuthor("Test Author 2")
                .setIsbn("2222222222222")
                .setPrice(BigDecimal.valueOf(15.99));

        BookDto thirdDto = createBookDto(3L, List.of(2L))
                .setTitle("Test Book 3")
                .setAuthor("Test Author 3")
                .setIsbn("3333333333333")
                .setPrice(BigDecimal.valueOf(20.99));

        return List.of(firstDto, secondDto, thirdDto);
    }
}
