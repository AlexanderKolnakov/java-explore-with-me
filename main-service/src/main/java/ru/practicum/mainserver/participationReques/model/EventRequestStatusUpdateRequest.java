package ru.practicum.mainserver.participationReques.model;


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

    @NotBlank(message = "Field: requestIds. Error: must not be blank. Value: {requestIds}")
    @NotNull(message = "Field: requestIds. Error: must not be blank. Value: {requestIds}")
    private List<Long> requestIds;   // Идентификаторы запросов на участие в событии текущего пользователя

    @NotBlank(message = "Field: status. Error: must not be blank. Value: {status}")
    @NotNull(message = "Field: status. Error: must not be blank. Value: {status}")
    private String status;   // Новый статус запроса на участие в событии текущего пользователя
}
