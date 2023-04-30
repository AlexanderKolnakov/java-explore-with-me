package ru.practicum.mainserver.category;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.category.models.CategoryDto;
import ru.practicum.mainserver.category.models.NewCategoryDto;
import ru.practicum.mainserver.exception.ApiError;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDto getCategoryById(Long catId) {

        return categoryRepository.findById(catId)
                .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Category with id=" + catId + " was not found",
                        LocalDateTime.now()));
    }

    public List<CategoryDto> getCategory(int from, int size) {

        Pageable pageable = PageRequest.of((from / size), size);

        return categoryRepository.findAll(pageable).toList();
    }

    @Transactional(rollbackOn = Exception.class)
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDto) {

        getCategoryById(catId);

        List<CategoryDto> categoryWhitNewName = categoryRepository.findCategoryByName(newCategoryDto.getName());
        if (!categoryWhitNewName.isEmpty()) {
            throw new DataIntegrityViolationException("Category with name=" + newCategoryDto.getName() + " already exists.");
        }

        CategoryDto createdCategory = new CategoryDto(catId, newCategoryDto.getName());
        return categoryRepository.save(createdCategory);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteCategory(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Category with id=" + catId + " was not found",
                        LocalDateTime.now()));

        // + проверка, что с категорией не свзяаны ни одного события.

        categoryRepository.deleteById(catId);
    }

    @Transactional(rollbackOn = Exception.class)
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {

        List<CategoryDto> categoryWhitNewName = categoryRepository.findCategoryByName(newCategoryDto.getName());
        if (!categoryWhitNewName.isEmpty()) {
            throw new DataIntegrityViolationException("Category with name=" + newCategoryDto.getName() + " already registered");
        }

        CategoryDto createdCategory = CategoryMapper.newCategoryToCategoryDto(newCategoryDto);

        return categoryRepository.save(createdCategory);
    }
}
