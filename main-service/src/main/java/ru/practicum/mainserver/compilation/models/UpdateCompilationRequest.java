package ru.practicum.mainserver.compilation.models;


import lombok.*;

import java.util.List;

public class UpdateCompilationRequest extends CompilationParent{

    // Изменение информации о подборке событий.

    private List<Long> events;   // Список id событий подборки для полной замены текущего списка

    private boolean pinned;   // Закреплена ли подборка на главной странице сайта

    private String title;   // Заголовок подборки
}
