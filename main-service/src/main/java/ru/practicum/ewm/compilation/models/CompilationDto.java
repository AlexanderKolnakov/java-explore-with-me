package ru.practicum.ewm.compilation.models;


import lombok.*;
import ru.practicum.ewm.event.model.EventShortDto;

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
