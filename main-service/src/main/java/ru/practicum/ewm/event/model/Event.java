package ru.practicum.ewm.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewm.category.models.CategoryDto;
import ru.practicum.ewm.location.LocationInMap;
import ru.practicum.ewm.user.models.UserDto;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EVENTS")
public class Event {

    // Событие

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // Идентификатор категории

    @Column(name = "ANNOTATION")
    private String annotation;   // Краткое описание

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private CategoryDto category;   // Категория

    @Column(name = "CONFIMED_REQUESTS")
    private Long confirmedRequests;   // Количество одобренных заявок на участие в данном событии

    @Column(name = "CREATED_ON")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;   // Дата и время создания события

    @Column(name = "DESCRIPTION")
    private String description;   // Полное описание события

    @Column(name = "EVENT_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;   // Дата и время на которые намечено событие

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserDto initiator; // Пользователь (краткая информация)

    @ManyToOne
    @JoinColumn(name = "LOCATION_IN_MAP_ID")
    private LocationInMap location;   // Место положение проведения мероприятия

    @Column(name = "PAID")
    private boolean paid;   // Нужно ли оплачивать участие

    @Column(name = "PARTICIPANT_LIMIT")
    private Long participantLimit;   // Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    @Column(name = "PUBLISHED_ON")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;   // Дата и время публикации события

    @Column(name = "REQUEST_MODERATION")
    private boolean requestModeration;   // Нужна ли пре-модерация заявок на участие

    @Column(name = "EVENTS_STATE")
    private String state;   // Список состояний жизненного цикла события

    @Column(name = "TITLE")
    private String title;   // Заголовок

    @Column(name = "VIEWS")
    private Long views;   // Количество просмотрев события
}
