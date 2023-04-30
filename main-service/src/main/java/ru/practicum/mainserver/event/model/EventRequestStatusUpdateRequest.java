package ru.practicum.mainserver.event.model;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    // Изменение статуса запроса на участие в событии текущего пользователя

    private List<Long> requestIds;   // Идентификаторы запросов на участие в событии текущего пользователя

    private String status;   // Новый статус запроса на участие в событии текущего пользователя
}
