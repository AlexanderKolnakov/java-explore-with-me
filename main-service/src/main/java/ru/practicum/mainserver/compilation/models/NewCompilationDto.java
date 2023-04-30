package ru.practicum.mainserver.compilation.models;


import lombok.*;

import javax.persistence.Entity;
import java.util.List;

public class NewCompilationDto extends CompilationParent{

    // Подборка событий

    private List<Long> events;   // Список идентификаторов событий входящих в подборку

    private boolean pinned;  // Закреплена ли подборка на главной странице сайта

    private String title;   // Заголовок подборки
}
