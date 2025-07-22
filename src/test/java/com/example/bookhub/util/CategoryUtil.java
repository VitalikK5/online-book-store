package com.example.bookhub.util;

import com.example.bookhub.dto.category.CategoryDto;
import com.example.bookhub.dto.category.CreateCategoryRequestDto;
import com.example.bookhub.model.Category;
import java.util.List;

public class CategoryUtil {
    public static CreateCategoryRequestDto createCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("Category 1")
                .setDescription("Test Category 1");
    }

    public static CreateCategoryRequestDto createEmptyCategoryRequestDto() {
        return new CreateCategoryRequestDto()
                .setName("")
                .setDescription("");
    }

    public static Category createCategory(Long id, String name, String description) {
        return new Category()
                .setId(id)
                .setName(name)
                .setDescription(description);
    }

    public static CategoryDto createCategoryDto(Long id) {
        return new CategoryDto()
                .setId(id)
                .setName("Category " + id)
                .setDescription("Test Category " + id);
    }

    public static CategoryDto createUpdatedCategoryDto(Long id) {
        return new CategoryDto()
                .setId(id)
                .setName("Updated Category " + id)
                .setDescription("Updated Test Category " + id);
    }

    public static List<CategoryDto> createListOfCategoryDtos() {
        return List.of(
                createCategoryDto(1L),
                createCategoryDto(2L),
                createCategoryDto(3L)
        );
    }
}
