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
public class UpdateEventAdminRequest {

    // Данные для изменения информации о событии.

    @Size(min = 20, max = 2000,
            message = "Новая аннотация должна быть не менее чем из 20 символов, но не более 2000.")
    private String annotation;   // Новая аннотация

    private int category;   // Новая категория

    @Size(min = 20, max = 7000,
            message = "Новое описание должно быть не менее чем из 20 символов, но не более 2000.")
    private String description;   // Новое описание

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;   // Новые дата и время на которые намечено событие.

    private Location location;

    private  boolean paid;   // Новое значение флага о платности мероприятия

    private int participantLimit;   // Новый лимит пользователей

    private boolean requestModeration;   // Нужна ли пре-модерация заявок на участие

    private String stateAction;   // Новое состояние события

    @Size(min = 3, max = 120,
            message = "Новый заголовок должн быть не менее чем из 20 символов, но не более 2000.")
    private String title;   // Новый заголовок

}