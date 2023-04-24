package ru.practicum.mainserver.category.models;


import lombok.*;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {

    // Данные для добавления новой категории

    private String name;   // Название категории
}
