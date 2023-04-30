package ru.practicum.mainserver.compilation.models;


import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {

    // Изменение информации о подборке событий.

    private List<Long> events;   // Список id событий подборки для полной замены текущего списка

    private boolean pinned;   // Закреплена ли подборка на главной странице сайта

    private String title;   // Заголовок подборки
}
