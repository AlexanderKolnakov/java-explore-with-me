package ru.practicum.mainserver.event;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.event.model.*;
import ru.practicum.mainserver.participationReques.model.EventRequestStatusUpdateRequest;
import ru.practicum.mainserver.participationReques.model.EventRequestStatusUpdateResult;
import ru.practicum.mainserver.participationReques.model.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EventService {
    public List<EventShortDto> getEvents
            (String text, int categories, Boolean paid, LocalDateTime rangeStart,
             LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {

        return null;
    }

    public EventFullDto getFullEventsById(int id) {
        return null;

    }

    public List<EventShortDto> getEventsByUserId(int userId, int from, int size) {
        return null;

    }

    public EventFullDto createEventByUser(int userId, NewEventDto newEventDto) {
        return null;

    }

    public EventFullDto getFullEventsByUserId(int userId, int eventId) {
        return null;

    }

    public EventFullDto updateFullEventsByUserId(UpdateEventUserRequest updateEventUserRequest, int userId, int eventId) {
        return null;

    }

    public List<ParticipationRequestDto> getParticipationRequestByUserId(int userId, int eventId) {
        return null;

    }

    public EventRequestStatusUpdateResult updateEventRequestStatusByUserId
            (EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, int userId, int eventId) {
        return null;
    }

    public List<EventFullDto> getEventsByAdmin
            (int users, String states, int categories, LocalDateTime rangeStart,
             LocalDateTime rangeEnd, int from, int size) {
        return null;

    }

    public EventFullDto updateEventsByAdmin(UpdateEventAdminRequest updateEventAdminRequest, int eventId) {
        return null;

    }
}
