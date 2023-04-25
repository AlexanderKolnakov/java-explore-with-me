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
@Table(name = "CATEGORYS", uniqueConstraints= @UniqueConstraint(columnNames={"ID", "CATEGORY_NAME"}))
public class CategoryDto {

    // Категория

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // Идентификатор категории

    @Column(name = "CATEGORY_NAME")
    @NotBlank(message = "Field: {name}. Error: must not be blank. Value: null")
    private String name;   // Название категории
}
