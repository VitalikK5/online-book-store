package com.example.bookhub.service;

import static com.example.bookhub.util.BookUtil.createBook;
import static com.example.bookhub.util.BookUtil.createBookDto;
import static com.example.bookhub.util.BookUtil.createBookRequestDto;
import static com.example.bookhub.util.BookUtil.createCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookhub.dto.book.BookDto;
import com.example.bookhub.dto.book.BookSearchParameters;
import com.example.bookhub.dto.book.CreateBookRequestDto;
import com.example.bookhub.exception.EntityNotFoundException;
import com.example.bookhub.mapper.BookMapper;
import com.example.bookhub.model.Book;
import com.example.bookhub.model.Category;
import com.example.bookhub.repository.book.BookRepository;
import com.example.bookhub.repository.book.BookSpecificationBuilder;
import com.example.bookhub.repository.category.CategoryRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("save should save and return BookDto")
    void save_returnsSavedBookDto() {
        CreateBookRequestDto requestDto = createBookRequestDto(List.of(1L));
        Book book = createBook(1L, Set.of());
        BookDto bookDto = createBookDto(1L, List.of(1L));
        List<Category> categories = List.of(createCategory(1L, "Test"));

        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(categoryRepository.findAllById(requestDto.getCategoryIds())).thenReturn(categories);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.save(requestDto);

        assertNotNull(result);
        assertEquals(bookDto, result);
        verify(bookMapper).toEntity(requestDto);
        verify(categoryRepository).findAllById(requestDto.getCategoryIds());
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("findAll should return page of BookDto")
    void findAll_returnsPageOfBookDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Book book = createBook(1L, Set.of());
        BookDto bookDto = createBookDto(1L, List.of(1L));
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(bookDto, result.getContent().get(0));
        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("findById should return BookDto when book exists")
    void findById_existingBook_returnsBookDto() {
        Long id = 1L;
        Book book = createBook(id, Set.of());
        BookDto bookDto = createBookDto(id, List.of(1L));

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.findById(id);

        assertNotNull(result);
        assertEquals(bookDto, result);
        verify(bookRepository).findById(id);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("findById should throw if book not found")
    void findById_bookNotFound_throwsException() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(id));

        assertEquals("Can not find book with id: " + id, ex.getMessage());
        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookMapper);
    }

    @Test
    @DisplayName("update should update and return BookDto")
    void update_existingBook_returnsUpdatedBookDto() {
        Long id = 1L;
        CreateBookRequestDto requestDto = createBookRequestDto(List.of(1L));
        Book book = createBook(id, Set.of());
        BookDto updatedDto = createBookDto(id, List.of(1L));

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        doNothing().when(bookMapper).updateFromDto(book, requestDto);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(updatedDto);

        BookDto result = bookService.update(id, requestDto);

        assertNotNull(result);
        assertEquals(updatedDto, result);
        verify(bookRepository).findById(id);
        verify(bookMapper).updateFromDto(book, requestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);
    }

    @Test
    @DisplayName("update should throw if book not found")
    void update_bookNotFound_throwsException() {
        Long id = 1L;
        CreateBookRequestDto requestDto = createBookRequestDto(List.of(1L));

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(id, requestDto));

        assertEquals("Book not found with id: " + id, ex.getMessage());
        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("deleteById should call repository when book exists")
    void deleteById_callsRepository() {
        Long id = 1L;

        when(bookRepository.existsById(id)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(id);

        bookService.deleteById(id);

        verify(bookRepository).existsById(id);
        verify(bookRepository).deleteById(id);
    }

    @Test
    @DisplayName("search should return matching books based on parameters")
    void search_withValidParams_returnsBooks() {
        Book book = createBook(1L, Set.of());
        BookDto bookDto = createBookDto(1L, List.of(1L));
        BookSearchParameters params = new BookSearchParameters(
                null, new String[]{"Test Author"}, null, null
        );

        Specification<Book> specification = (
                Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder cb
        ) -> cb.conjunction();

        when(bookSpecificationBuilder.build(params)).thenReturn(specification);
        when(bookRepository.findAll(specification)).thenReturn(List.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> result = bookService.search(params);

        assertEquals(1, result.size());
        assertEquals(bookDto, result.get(0));
        verify(bookSpecificationBuilder).build(params);
        verify(bookRepository).findAll(specification);
        verify(bookMapper).toDto(book);
    }
}
