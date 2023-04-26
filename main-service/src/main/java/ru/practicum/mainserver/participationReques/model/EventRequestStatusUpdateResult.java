package ru.practicum.mainserver.participationReques.model;


import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {

    // Результат подтверждения/отклонения заявок на участие в событии

    private List<ParticipationRequestDto> confirmedRequests; // Заявка на участие в событии

    private List<ParticipationRequestDto> rejectedRequests; // Заявка на участие в событии


}
