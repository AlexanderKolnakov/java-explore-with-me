package ru.practicum.mainserver.event.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.mainserver.category.models.CategoryDto;
import ru.practicum.mainserver.user.models.UserShortDto;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name = "HITS")
public class EventShortDto {

    // Краткая информация о событии

    private String annotation;   // Краткое описание

    private CategoryDto category;   //   Категория

    private int confirmedRequests;   // Количество одобренных заявок на участие в данном событии

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;   // Дата и время на которые намечено событие

    private int id;   // Идентификатор события

    private UserShortDto initiator; // Пользователь (краткая информация)

    private boolean paid;   // Нужно ли оплачивать участие

    private String title;   // Заголовок

    private String views;   // Количество просмотрев события
}
