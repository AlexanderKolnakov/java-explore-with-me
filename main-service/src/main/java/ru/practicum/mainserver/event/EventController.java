package ru.practicum.mainserver.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainserver.compilation.models.CompilationDto;
import ru.practicum.mainserver.compilation.models.NewCompilationDto;
import ru.practicum.mainserver.event.model.EventFullDto;
import ru.practicum.mainserver.event.model.EventShortDto;
import ru.practicum.mainserver.event.model.NewEventDto;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping()
public class EventController {

    private final EventService eventService;

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsByUserId(
            @PathVariable int userId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен GET запрос на получение событий, добавленных пользователем c id - " + userId);
        return eventService.getEventsByUserId(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEventByUser(@RequestBody NewEventDto newEventDto) {
        log.debug("Получен POST запрос на создание нового события от пользователя с id - " + );
        return compilationService.createCompilation(newCompilationDto);
    }
}
