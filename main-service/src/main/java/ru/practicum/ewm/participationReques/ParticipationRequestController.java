package ru.practicum.ewm.participationReques;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.participationReques.model.ParticipationRequestDto;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping()
public class ParticipationRequestController {

    private final ParticipationRequestService participationRequestService;

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestByUserId(@PathVariable Long userId) {
        log.info("Получен GET запрос на получение информации о заявках пользователя с id - " + userId +
                " на участие в чужих событиях.");
        return participationRequestService.getParticipationRequestByUserId(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(@PathVariable Long userId,
                                                              @RequestParam Long eventId) {

        log.info("Получен POST запрос на создание запроса от пользователя с id - " + userId +
                " на участие в событии с id - " + eventId);
        return participationRequestService.createParticipationRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequestByUserId(
            @PathVariable Long userId,
            @PathVariable Long requestId) {
        log.info("Получен PATCH запрос на отмену своего запроса на участие событии с id " + requestId +
                " от пользователем c id - " + userId);
        return participationRequestService.cancelParticipationRequestByUserId(userId, requestId);
    }


}
