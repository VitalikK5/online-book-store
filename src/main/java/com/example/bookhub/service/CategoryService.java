package com.example.bookhub.service;

import com.example.bookhub.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookhub.dto.category.CategoryDto;
import com.example.bookhub.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto requestDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);

    Page<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable);
}
