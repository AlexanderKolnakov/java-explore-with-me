package ru.practicum.mainserver.category;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.category.models.CategoryDto;
import ru.practicum.mainserver.category.models.NewCategoryDto;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    public CategoryDto getCategoryById(int catId) {
        return null;
    }

    public List<CategoryDto> getCategory(int from, int size) {
        return null;

    }

    public CategoryDto updateCategory(int catId, NewCategoryDto newCategoryDto) {
        return null;

    }

    public void deleteCategory(int catId) {
    }

    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        return null;

    }
}
