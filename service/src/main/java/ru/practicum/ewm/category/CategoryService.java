package ru.practicum.ewm.category;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.FromIndexPageRequest;
import ru.practicum.ewm.common.exception.NotFoundException;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CategoryRepository categoryRepository;

    private Category findCategoryById(long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Category with id=%s was not found.", catId))
                );
    }

    public CategoryDto create(CategoryDto categoryDto) {
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.fromCategoryDto(categoryDto)));
    }

    public CategoryDto update(CategoryDto categoryDto) {
        findCategoryById(categoryDto.getId());
        Category category = CategoryMapper.fromCategoryDto(categoryDto);
        return CategoryMapper.toCategoryDto(category);
    }

    public void remove(Long catId) {
        findCategoryById(catId);
        categoryRepository.deleteById(catId);
    }

    public Collection<CategoryDto> getAll(int from, int size) {
        final Pageable pageable = FromIndexPageRequest.of(from, size);

        return CategoryMapper.toCategoryDtos(categoryRepository.findAll(pageable).toList());
    }

    public CategoryDto getById(long catId) {
        return CategoryMapper.toCategoryDto(findCategoryById(catId));
    }
}
