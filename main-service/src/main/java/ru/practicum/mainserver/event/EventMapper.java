package ru.practicum.mainserver.event;

import org.springframework.stereotype.Component;
import ru.practicum.mainserver.category.models.CategoryDto;
import ru.practicum.mainserver.event.model.*;
import ru.practicum.mainserver.user.models.NewUserRequest;
import ru.practicum.mainserver.user.models.UserDto;
import ru.practicum.mainserver.user.models.UserShortDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class EventMapper {

    public static Event newEventDtoToEvent(NewEventDto newEventDto, UserDto user, CategoryDto category) {
        Event event = new Event();

        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);

        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setInitiator(user);
        event.setLon(newEventDto.getLocation().getLon());
        event.setLat(newEventDto.getLocation().getLat());
        event.setPaid(newEventDto.isPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setTitle(newEventDto.getTitle());

        return event;
    }

    public static EventFullDto eventToEventFullDto(Event createdEvent) {
        EventFullDto eventFullDto = new EventFullDto();

        eventFullDto.setId(createdEvent.getId());
        eventFullDto.setAnnotation(createdEvent.getAnnotation());
        eventFullDto.setCategory(createdEvent.getCategory());
        eventFullDto.setConfirmedRequests(createdEvent.getConfirmedRequests());
        eventFullDto.setCreatedOn(createdEvent.getCreatedOn());
        eventFullDto.setDescription(createdEvent.getDescription());
        eventFullDto.setEventDate(createdEvent.getEventDate());
        eventFullDto.setInitiator(new UserShortDto(createdEvent.getInitiator().getId(),
                createdEvent.getInitiator().getName()));
        eventFullDto.setLocation(new Location(createdEvent.getLat(), createdEvent.getLon()));
        eventFullDto.setPaid(createdEvent.isPaid());
        eventFullDto.setParticipantLimit(createdEvent.getParticipantLimit());

        eventFullDto.setPublishedOn(createdEvent.getPublishedOn());  // ???

        eventFullDto.setRequestModeration(createdEvent.isRequestModeration());
        eventFullDto.setState(createdEvent.getState());
        eventFullDto.setTitle(createdEvent.getTitle());
        eventFullDto.setViews(createdEvent.getViews());

        return eventFullDto;
    }

    public static List<EventShortDto> listEventToListEventShortDto(List<Event> eventList) {
        List<EventShortDto> resultList = new ArrayList<>();

        for (Event event: eventList) {
            resultList.add(eventToEventShortDto(event));
        }
        return resultList;
    }

    public static EventShortDto eventToEventShortDto (Event event) {
        EventShortDto eventShortDto = new EventShortDto();

        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(event.getCategory());
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setId(event.getId());
        eventShortDto.setInitiator(new UserShortDto(event.getInitiator().getId(),
                event.getInitiator().getName()));
        eventShortDto.setPaid(event.isPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(event.getViews());

        return eventShortDto;
    }

    public static List<EventFullDto> listEventToListEventFullDto(List<Event> eventList) {
        List<EventFullDto> eventFullDtoList = new ArrayList<>();

        for (Event event: eventList) {
            eventFullDtoList.add(eventToEventFullDto(event));
        }
        return eventFullDtoList;
    }
}
