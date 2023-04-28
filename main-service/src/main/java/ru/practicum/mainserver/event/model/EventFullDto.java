package ru.practicum.mainserver.event.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.stereotype.Component;
import ru.practicum.mainserver.category.models.CategoryDto;
import ru.practicum.mainserver.user.models.UserShortDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor

public class EventFullDto {

    // Событие

    private Long id;   // Идентификатор категории

    private String annotation;   // Краткое описание

    private CategoryDto category;   // Категория

    private Long confirmedRequests;   // Количество одобренных заявок на участие в данном событии

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;   // Дата и время создания события

    private String description;   // Полное описание события

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;   // Дата и время на которые намечено событие

    private UserShortDto initiator; // Пользователь (краткая информация)

    private Location location;   //   Широта и долгота места проведения события

    private boolean paid;   // Нужно ли оплачивать участие

    private Long participantLimit;   // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;   // Дата и время публикации события

    private boolean requestModeration;   // Нужна ли пре-модерация заявок на участие

    private String state;   // Список состояний жизненного цикла события

    private String title;   // Заголовок

    private Long views;   // Количество просмотрев события
}
