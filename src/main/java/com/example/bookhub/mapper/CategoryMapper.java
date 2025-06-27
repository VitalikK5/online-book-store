package com.example.bookhub.mapper;

import com.example.bookhub.config.MapperConfig;
import com.example.bookhub.dto.category.CategoryDto;
import com.example.bookhub.dto.category.CreateCategoryRequestDto;
import com.example.bookhub.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateCategoryFromDto(CategoryDto categoryDto, @MappingTarget Category category);
}
