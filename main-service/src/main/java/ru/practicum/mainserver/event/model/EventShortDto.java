package ru.practicum.mainserver.event.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.mainserver.category.models.CategoryDto;
import ru.practicum.mainserver.user.models.UserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor

public class EventShortDto {

    // Краткая информация о событии

    private String annotation;   // Краткое описание

    private CategoryDto category;   //   Категория

    private Long confirmedRequests;   // Количество одобренных заявок на участие в данном событии

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;   // Дата и время на которые намечено событие

    private Long id;   // Идентификатор события

    private UserShortDto initiator; // Пользователь (краткая информация)

    private boolean paid;   // Нужно ли оплачивать участие

    private String title;   // Заголовок

    private Long views;   // Количество просмотрев события
}
