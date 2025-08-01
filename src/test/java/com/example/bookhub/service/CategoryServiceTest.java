package com.example.bookhub.service;

import static com.example.bookhub.util.BookUtil.createBook;
import static com.example.bookhub.util.BookUtil.createBookDtoWithoutCategoryIds;
import static com.example.bookhub.util.CategoryUtil.createCategory;
import static com.example.bookhub.util.CategoryUtil.createCategoryDto;
import static com.example.bookhub.util.CategoryUtil.createCategoryRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.bookhub.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookhub.dto.category.CategoryDto;
import com.example.bookhub.dto.category.CreateCategoryRequestDto;
import com.example.bookhub.exception.EntityNotFoundException;
import com.example.bookhub.mapper.BookMapper;
import com.example.bookhub.mapper.CategoryMapper;
import com.example.bookhub.model.Book;
import com.example.bookhub.model.Category;
import com.example.bookhub.repository.book.BookRepository;
import com.example.bookhub.repository.category.CategoryRepository;
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
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("Get category by id with existing id returns CategoryDto")
    void getById_withValidId_returnsCategoryDto() {
        Long categoryId = 1L;
        Category category = createCategory(categoryId, "Fiction", "Fiction books");
        CategoryDto expected = createCategoryDto(categoryId);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.getById(categoryId);
        assertEquals(expected, actual);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Get category by id with invalid id throws EntityNotFoundException")
    void getById_withInvalidId_throwsException() {
        Long categoryId = 999L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(categoryId));
        verify(categoryRepository).findById(categoryId);
    }

    @Test
    @DisplayName("Save valid category returns CategoryDto")
    void save_validRequest_returnsCategoryDto() {
        CreateCategoryRequestDto requestDto = createCategoryRequestDto();
        Category category = createCategory(1L, requestDto.getName(),
                requestDto.getDescription());
        CategoryDto expected = createCategoryDto(1L);
        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.save(requestDto);
        assertEquals(expected, actual);
        verify(categoryMapper).toEntity(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Update valid category returns CategoryDto")
    void update_withValidId_returnsUpdatedCategoryDto() {
        Long categoryId = 1L;
        CategoryDto expected = createCategoryDto(categoryId);
        expected.setName("Updated Fiction");
        expected.setDescription("Updated description");
        Category category = createCategory(categoryId, "Fiction", "Fiction books");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategoryFromDto(expected, category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.update(categoryId, expected);
        assertEquals(expected, actual);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateCategoryFromDto(expected, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Delete category by id")
    void deleteById_withValidId_success() {
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(categoryId);
        categoryService.deleteById(categoryId);
        verify(categoryRepository).existsById(categoryId);
        verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    @DisplayName("Find all categories returns Page<CategoryDto>")
    void findAll_returnsPageOfCategoryDtos_success() {
        Pageable pageable = PageRequest.of(0, 10);
        Category category = createCategory(1L, "Fiction", "Fiction books");
        CategoryDto categoryDto = createCategoryDto(1L);
        Page<Category> pageOfCategories = new PageImpl<>(List.of(category), pageable, 1);
        when(categoryRepository.findAll(pageable)).thenReturn(pageOfCategories);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        Page<CategoryDto> actual = categoryService.findAll(pageable);
        assertNotNull(actual);
        assertEquals(1, actual.getContent().size());
        assertEquals(categoryDto, actual.getContent().get(0));
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("findAllByCategoryId returns page of BookDtoWithoutCategoryIds")
    void findAllByCategoryId_returnsPageOfBookDtoWithoutCategoryIds() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Book book = createBook(1L, Set.of());
        BookDtoWithoutCategoryIds dto = createBookDtoWithoutCategoryIds(1L);
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAllByCategories_Id(categoryId, pageable)).thenReturn(bookPage);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(dto);
        Page<BookDtoWithoutCategoryIds> result = categoryService.findAllByCategoryId(categoryId,
                pageable);
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(dto, result.getContent().get(0));
        verify(bookRepository, times(1)).findAllByCategories_Id(categoryId, pageable);
        verify(bookMapper, times(1)).toDtoWithoutCategories(book);
    }

}
