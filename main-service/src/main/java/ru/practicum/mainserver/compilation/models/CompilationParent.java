package ru.practicum.mainserver.compilation.models;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class CompilationParent {

    private List<Long> events;   // Список идентификаторов событий входящих в подборку

    private boolean pinned;  // Закреплена ли подборка на главной странице сайта

    @NotBlank(message = "Field: title. Error: must not be blank. Value: {title}")
    @NotNull(message = "Field: title. Error: must not be blank. Value: {title}")
    private String title;   // Заголовок подборки
}
