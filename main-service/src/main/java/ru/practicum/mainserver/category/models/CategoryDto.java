package ru.practicum.mainserver.category.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name = "HITS")
public class CategoryDto {

    // Категория

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // Идентификатор категории

    @NotBlank(message = "Название категории не может быть пустым или Null")
    private String name;   // Название категории
}
