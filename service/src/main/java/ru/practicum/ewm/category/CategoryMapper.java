package ru.practicum.ewm.category;

import java.util.Collection;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.of(category.getId(), category.getName());
    }

    public static Category fromCategoryDto(CategoryDto categoryDto) {
        return Category.of(categoryDto.getId(), categoryDto.getName());
    }

    public static Collection<CategoryDto> toCategoryDtos(Collection<Category> categories) {
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }
}
