package ru.practicum.mainserver.compilation.models;


import lombok.*;
import ru.practicum.mainserver.event.model.EventShortDto;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    // Подборка событий


    private Long id;   // Идентификатор подборки

    private List<EventShortDto> events;   // Список событий входящих в подборку

    private boolean pinned;   // Закреплена ли подборка на главной странице сайта

    private String title;   // Заголовок подборки
}
