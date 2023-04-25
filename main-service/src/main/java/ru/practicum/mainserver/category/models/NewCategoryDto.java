package ru.practicum.mainserver.category.models;


import lombok.*;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {

    // Данные для добавления новой категории

    @NotBlank(message = "Field: {name}. Error: must not be blank. Value: null")
    private String name;   // Название категории
}
