package ru.practicum.mainserver.event;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.category.CategoryService;
import ru.practicum.mainserver.category.models.CategoryDto;
import ru.practicum.mainserver.event.enums.StateEvent;
import ru.practicum.mainserver.event.model.*;
import ru.practicum.mainserver.exception.ApiError;
import ru.practicum.mainserver.participationReques.model.EventRequestStatusUpdateRequest;
import ru.practicum.mainserver.participationReques.model.EventRequestStatusUpdateResult;
import ru.practicum.mainserver.participationReques.model.ParticipationRequestDto;
import ru.practicum.mainserver.user.UserRepository;
import ru.practicum.mainserver.user.UserService;
import ru.practicum.mainserver.user.models.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    public List<EventShortDto> getEvents
            (String text, Long categories, Boolean paid, LocalDateTime rangeStart,
             LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {

        return null;
    }

    public EventFullDto getFullEventsById(Long id) {
        return null;

    }

    public List<EventShortDto> getEventsByUserId(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of((from / size), size);

        List<Event> eventList = eventRepository.findEventByCreatedUserId(userId, pageable);
        return EventMapper.listEventToListEventShortDto(eventList);
    }

    public EventFullDto createEventByUser(Long userId, NewEventDto newEventDto) {
        checkDataTime(newEventDto.getEventDate());
        CategoryDto category = categoryService.getCategoryById(newEventDto.getCategory());
        UserDto user = userRepository.findById(userId).orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                "User with id=" + userId + " was not found",
                LocalDateTime.now()));

        Event createdEvent = EventMapper.newEventDtoToEvent(newEventDto, user, category);
        createdEvent.setConfirmedRequests(0L);
        createdEvent.setState(StateEvent.PENDING.toString());

        eventRepository.save(createdEvent);
        return EventMapper.eventToEventFullDto(createdEvent);
    }



    public EventFullDto getFullEventsByUserId(Long userId, Long eventId) {
        return null;

    }

    public EventFullDto updateFullEventsByUserId(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId) {
        return null;

    }

    public List<ParticipationRequestDto> getParticipationRequestByUserId(Long userId, Long eventId) {
        return null;

    }

    public EventRequestStatusUpdateResult updateEventRequestStatusByUserId
            (EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, Long userId, Long eventId) {
        return null;
    }

    public List<EventFullDto> getEventsByAdmin
            (Long users, String states, Long categories, LocalDateTime rangeStart,
             LocalDateTime rangeEnd, int from, int size) {
        return null;

    }

    public EventFullDto updateEventsByAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        return null;

    }


    private void checkDataTime(LocalDateTime eventDate) {
        if (eventDate.plusHours(2).isAfter(LocalDateTime.now())) {
            throw new DataIntegrityViolationException("Field: eventDate. Error: должно содержать дату, которая еще " +
                    "не наступила. Value: " + eventDate);
        }
    }
}
