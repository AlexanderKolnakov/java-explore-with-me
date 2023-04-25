package ru.practicum.mainserver.event.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    //  Новое событие

    @Size(min = 20, max = 2000,
            message = "Описание события должно быть не менее чем из 20 символов, но не более 2000.")
    private String annotation;  // Краткое описание события

    private Long category;   // id категории к которой относится событие

    @Size(min = 20, max = 7000,
            message = "Полное описание события должно быть не менее чем из 20 символов, но не более 7000.")
    private String description;   // Полное описание события

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;   // Дата и время на которые намечено событие.

    private Location location;

    private boolean paid;   // Нужно ли оплачивать участие в событии

    private Long participantLimit;   // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    private boolean requestModeration;   // Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически.

    @Size(min = 3, max = 120,
            message = "Заголовок события должно быть не менее чем из 3 символов, но не более 120.")
    private String title;   // Заголовок события
}
