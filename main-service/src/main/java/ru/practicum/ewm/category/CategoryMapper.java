package ru.practicum.ewm.category;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.models.CategoryDto;
import ru.practicum.ewm.category.models.NewCategoryDto;

@Component
public class CategoryMapper {

    public static CategoryDto newCategoryToCategoryDto(NewCategoryDto newCategoryDto) {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(newCategoryDto.getName());
        return categoryDto;
    }
}
