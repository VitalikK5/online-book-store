package com.example.bookhub.service;

import com.example.bookhub.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookhub.dto.category.CategoryDto;
import com.example.bookhub.dto.category.CreateCategoryRequestDto;
import com.example.bookhub.exception.EntityNotFoundException;
import com.example.bookhub.mapper.BookMapper;
import com.example.bookhub.mapper.CategoryMapper;
import com.example.bookhub.model.Category;
import com.example.bookhub.repository.book.BookRepository;
import com.example.bookhub.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id: " + id)
        );
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        Category category = categoryMapper.toEntity(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category by id: " + id)
        );
        categoryMapper.updateCategoryFromDto(categoryDto, category);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Can't find category by id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId,
                                                               Pageable pageable) {
        return bookRepository.findAllByCategories_Id(categoryId, pageable)
                .map(bookMapper::toDtoWithoutCategories);
    }
}
