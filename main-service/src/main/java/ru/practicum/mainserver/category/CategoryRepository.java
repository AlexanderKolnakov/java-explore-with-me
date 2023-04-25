package ru.practicum.mainserver.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainserver.category.models.CategoryDto;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryDto, Long> {


    @Query("select c from CategoryDto c " +
            "where c.name =? 1")
    List<CategoryDto> findCategoryByName(String name);
}
