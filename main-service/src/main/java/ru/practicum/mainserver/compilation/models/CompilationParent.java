package ru.practicum.mainserver.compilation.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class CompilationParent {

    private List<Long> events;   // Список идентификаторов событий входящих в подборку

    private boolean pinned;  // Закреплена ли подборка на главной странице сайта

    private String title;   // Заголовок подборки
}
