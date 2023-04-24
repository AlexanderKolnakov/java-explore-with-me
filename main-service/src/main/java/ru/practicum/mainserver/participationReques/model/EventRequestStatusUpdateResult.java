package ru.practicum.mainserver.participationReques.model;


import lombok.*;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {

    // Результат подтверждения/отклонения заявок на участие в событии

    private ParticipationRequestDto confirmedRequests; // Заявка на участие в событии

    private ParticipationRequestDto rejectedRequests; // Заявка на участие в событии


}
