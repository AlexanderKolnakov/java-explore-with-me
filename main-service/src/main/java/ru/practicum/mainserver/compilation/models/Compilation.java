package ru.practicum.mainserver.compilation.models;


import lombok.*;
import ru.practicum.mainserver.event.model.Event;
import ru.practicum.mainserver.event.model.EventShortDto;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMPILATION")
public class Compilation {

    // Подборка событий

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // Идентификатор подборки

//    @ManyToMany
//    @JoinTable(
//            name = "COMPILATIONS_EVENTS",
//            joinColumns = @JoinColumn(name = "COMPILATION_ID"),
//            inverseJoinColumns = @JoinColumn(name = "EVENT_ID"))
//    private List<Event> events;   // Список событий входящих в подборку

    private boolean pinned;   // Закреплена ли подборка на главной странице сайта

    private String title;   // Заголовок подборки
}
