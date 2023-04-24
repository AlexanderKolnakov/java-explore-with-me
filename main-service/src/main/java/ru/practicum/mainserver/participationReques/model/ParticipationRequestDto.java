package ru.practicum.mainserver.participationReques.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
//@Table(name = "HITS")
public class ParticipationRequestDto {

    // Заявка на участие в событии

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;   // Дата и время создания заявки

    private int event;   // Идентификатор события

    private int id;   // Идентификатор заявки

    private int requester;   // Идентификатор пользователя, отправившего заявку

    private String status;   // Статус заявки
}
