package ru.practicum.mainserver.participationReques.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PARTICIPATION_REQUESTS")
public class ParticipationRequestDto {

    // Заявка на участие в событии

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // Идентификатор заявки

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;   // Дата и время создания заявки

    private Long event;   // Идентификатор события

    private Long requester;   // Идентификатор пользователя, отправившего заявку

    private String status;   // Статус заявки
}
