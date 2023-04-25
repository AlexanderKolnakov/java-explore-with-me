package ru.practicum.mainserver.category;

import org.springframework.stereotype.Component;
import ru.practicum.mainserver.category.models.CategoryDto;
import ru.practicum.mainserver.category.models.NewCategoryDto;
import ru.practicum.mainserver.user.models.NewUserRequest;
import ru.practicum.mainserver.user.models.UserDto;

@Component
public class CategoryMapper {

    public static CategoryDto newCategoryToCategoryDto(NewCategoryDto newCategoryDto) {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(newCategoryDto.getName());
        return categoryDto;
    }
}
