package ru.practicum.mainserver.participationReques;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainserver.event.model.EventFullDto;
import ru.practicum.mainserver.event.model.UpdateEventUserRequest;
import ru.practicum.mainserver.participationReques.model.ParticipationRequestDto;
import ru.practicum.mainserver.user.models.NewUserRequest;
import ru.practicum.mainserver.user.models.UserDto;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class ParticipationRequestController {

    private final ParticipationRequestService participationRequestService;

    @GetMapping()
    public List<ParticipationRequestDto> getParticipationRequestByUserId(@PathVariable int userId) {
        log.info("Получен GET запрос на получение информации о заявках пользователя с id - " + userId +
                " на участие в чужих событиях.");
        return participationRequestService.getParticipationRequestByUserId(userId);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationRequest(@PathVariable int userId,
                                                                 @RequestParam int eventId) {
        log.debug("Получен POST запрос на создание запроса от пользователя с id - " + userId +
                " на участие в событии с id - " + eventId);
        return participationRequestService.createParticipationRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequestByUserId(
            @PathVariable int userId,
            @PathVariable int requestId) {
        log.debug("Получен PATCH запрос на отмену своего запроса на участие событии с id " + requestId +
                " от пользователем c id - " + userId);
        return participationRequestService.cancelParticipationRequestByUserId(userId, requestId);
    }


}
