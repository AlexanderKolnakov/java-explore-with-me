package ru.practicum.mainserver.category;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainserver.category.models.CategoryDto;
import ru.practicum.mainserver.category.models.NewCategoryDto;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping()
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.debug("ADMIN. Получен POST запрос на добавление новой категории.");
        return categoryService.createCategory(newCategoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int catId) {
        log.debug("ADMIN. Получен DELETE запрос на удаление категории с id: {}.", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategory(
            @PathVariable int catId, @RequestBody NewCategoryDto newCategoryDto) {
        log.debug("ADMIN. Получен PATCH запрос на обновление категории с id: " + catId);
        return categoryService.updateCategory(catId, newCategoryDto);
    }

    @GetMapping("/categories")
    public List<CategoryDto> getCategory(
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен GET запрос на получение списка категорий.");
        return categoryService.getCategory(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable int catId) {
        log.info("Получен GET запрос на получение категории c id - + " + catId);
        return categoryService.getCategoryById(catId);
    }
}
