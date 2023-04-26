package ru.practicum.mainserver.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainserver.event.model.*;
import ru.practicum.mainserver.event.model.EventRequestStatusUpdateRequest;
import ru.practicum.mainserver.event.model.EventRequestStatusUpdateResult;
import ru.practicum.mainserver.participationReques.model.ParticipationRequestDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping()
public class EventController {

    private final EventService eventService;

    @GetMapping("/events")
    public List<EventShortDto> getEvents(
            @RequestParam String text,
            @RequestParam Long categories,
            @RequestParam("paid") Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam("onlyAvailable") Boolean onlyAvailable,
            @RequestParam String sort,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен GET запрос на получение списка событий по параметрам");
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getFullEventsById(@PathVariable Long id) {
        log.info("Получен GET запрос на получение информации о событий c id - " + id);
        return eventService.getFullEventsById(id);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsByUserId(
            @PathVariable Long userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен GET запрос на получение списка событий, добавленных пользователем c id - " + userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEventByUser(@PathVariable Long userId,
                                          @RequestBody @Valid NewEventDto newEventDto) {
        log.debug("Получен POST запрос на создание нового события от пользователя с id - " + userId);
        return eventService.createEventByUser(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getFullEventsByUserId(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        log.info("Получен GET запрос на получение полной информации о событии с id " + eventId + ", " +
                "добавленной пользователем c id - " + userId);
        return eventService.getFullEventsByUserId(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateFullEventsByUserId(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody  @Valid UpdateEventUserRequest updateEventUserRequest) {
        log.debug("Получен PATCH запрос на обновление информации о событии с id " + eventId + ", " +
                "добавленной пользователем c id - " + userId);
        return eventService.updateFullEventsByUserId(updateEventUserRequest, userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestByUserId(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        log.info("Получен GET запрос на получение информации о запросах на " +
                "участие в событии с id " + eventId + ", " +
                "от пользователем c id - " + userId);
        return eventService.getParticipationRequestByUserId(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventRequestStatusByUserId(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody  @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.debug("Получен PATCH запрос на обновление информации о событии с id " + eventId + ", " +
                "добавленной пользователем c id - " + userId);
        return eventService.updateEventRequestStatusByUserId(eventRequestStatusUpdateRequest, userId, eventId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsByAdmin(
            @RequestParam List<Long> users,
            @RequestParam List<String> states,
            @RequestParam List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("ADMIN. Получен GET запрос на получение списка событий по параметрам.");
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventsByAdmin(
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.debug("ADMIN. Получен PATCH запрос на обновление информации о событии с id " + eventId);
        return eventService.updateEventsByAdmin(updateEventAdminRequest, eventId);
    }
}
