package ru.practicum.ewm.category.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.CategoryDto;
import ru.practicum.ewm.category.CategoryService;
import ru.practicum.ewm.common.validation.group.Create;
import ru.practicum.ewm.common.validation.group.Update;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class AdminCategoryController {

    CategoryService categoryService;

    @PostMapping
    public CategoryDto create(@RequestBody @Validated(Create.class) @NotNull CategoryDto categoryDto) {
        return categoryService.create(categoryDto);
    }

    @PatchMapping
    public CategoryDto update(@RequestBody @Validated(Update.class) @NotNull CategoryDto categoryDto) {
        return categoryService.update(categoryDto);
    }

    @DeleteMapping("/{catId}")
    public void remove(@PathVariable Long catId) {
        categoryService.remove(catId);
    }
}
