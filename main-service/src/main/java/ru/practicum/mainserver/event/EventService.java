package ru.practicum.mainserver.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.category.CategoryRepository;
import ru.practicum.mainserver.category.CategoryService;
import ru.practicum.mainserver.category.models.CategoryDto;
import ru.practicum.mainserver.event.enums.SortEvent;
import ru.practicum.mainserver.event.enums.StateActionByAdmin;
import ru.practicum.mainserver.event.enums.StateEvent;
import ru.practicum.mainserver.event.model.*;
import ru.practicum.mainserver.exception.ApiError;
import ru.practicum.mainserver.participationReques.ParticipationRequestRepository;
import ru.practicum.mainserver.participationReques.enums.StatusRequest;
import ru.practicum.mainserver.participationReques.model.ParticipationRequestDto;
import ru.practicum.mainserver.user.UserRepository;
import ru.practicum.mainserver.user.models.UserDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    public List<EventShortDto> getEvents
            (String text, Long categories, Boolean paid, LocalDateTime rangeStart,
             LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {

        Pageable pageable = PageRequest.of((from / size), size);
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        List<Event> resultList;

        if (onlyAvailable) {

            resultList = eventRepository
                    .findPublicEventWhitParametersByUserWhereEventAvailable(rangeStart, rangeEnd,
                            StateEvent.PUBLISHED.toString(), categories, paid, text, pageable)
                    .orElse(Collections.emptyList());
        } else {
            resultList = eventRepository
                    .findPublicEventWhitParametersByUser(rangeStart, rangeEnd,
                            StateEvent.PUBLISHED.toString(), categories, paid, text, pageable)
                    .orElse(Collections.emptyList());
        }
        if (sort.equals(SortEvent.EVENT_DATE.toString())) {
            resultList.stream()
                    .sorted(Comparator.comparing(Event::getEventDate).reversed())
                    .collect(Collectors.toList());
        }
        if (sort.equals(SortEvent.VIEWS.toString())) {
            resultList.stream()
                    .sorted(Comparator.comparing(Event::getViews).reversed())
                    .collect(Collectors.toList());
        }


        // НУЖНО ПОДКЛЮЧИТЬ СЕРВИС СТАТИСТИКИ

        return EventMapper.listEventToListEventShortDto(resultList);
    }

    public EventFullDto getFullEventsById(Long id) {

        // НУЖНО ПОДКЛЮЧИТЬ СЕРВИС СТАТИСТИКИ


        return null;

    }

    public List<EventShortDto> getEventsByUserId(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of((from / size), size);

        List<Event> eventList = eventRepository.findEventByCreatedUserId(userId, pageable)
                .orElse(Collections.emptyList());
        if (eventList.isEmpty()) {
            return Collections.emptyList();
        } else {
            return EventMapper.listEventToListEventShortDto(eventList);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public EventFullDto createEventByUser(Long userId, NewEventDto newEventDto) {
        checkDataTime(newEventDto.getEventDate());
        UserDto user = checkUserById(userId);
        CategoryDto category = categoryService.getCategoryById(newEventDto.getCategory());

        Event createdEvent = EventMapper.newEventDtoToEvent(newEventDto, user, category);
        createdEvent.setConfirmedRequests(0L);
        createdEvent.setPublishedOn(LocalDateTime.now());
        createdEvent.setState(StateEvent.PENDING.toString());
        createdEvent.setViews(0L);

        eventRepository.save(createdEvent);
        return EventMapper.eventToEventFullDto(createdEvent);
    }


    public EventFullDto getFullEventsByUserId(Long userId, Long eventId) {
        checkUserById(userId);
        Event event = checkEventById(eventId);
        return checkEventInitiator(event, userId);
    }

    @Transactional(rollbackOn = Exception.class)
    public EventFullDto updateFullEventsByUserId
            (UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId) {

        checkUserById(userId);
        CategoryDto category = checkCategoryById(updateEventUserRequest.getCategory());
        Event event = checkEventById(eventId);
        if (event.getState().equals(StateEvent.PENDING.toString()) || event.getState().equals(StateEvent.CANCELED.toString())) {

            event.setAnnotation(updateEventUserRequest.getAnnotation());
            event.setCategory(category);
            event.setDescription(updateEventUserRequest.getDescription());
            event.setEventDate(updateEventUserRequest.getEventDate());
            event.setLat(updateEventUserRequest.getLocation().getLat());
            event.setLon(updateEventUserRequest.getLocation().getLon());
            event.setPaid(updateEventUserRequest.isPaid());
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
            event.setRequestModeration(updateEventUserRequest.isRequestModeration());
            event.setState(updateEventUserRequest.getStateAction());
            event.setTitle(updateEventUserRequest.getTitle());

            eventRepository.save(event);

            return EventMapper.eventToEventFullDto(event);
        } else {
            throw new DataIntegrityViolationException("Only pending or canceled events can be changed");
        }
    }


    public List<ParticipationRequestDto> getParticipationRequestByUserId(Long userId, Long eventId) {

        return participationRequestRepository.findByUserAndEventId(userId, eventId).orElse(Collections.emptyList());
    }

    public EventRequestStatusUpdateResult updateEventRequestStatusByUserId
            (EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest, Long userId, Long eventId) {

        checkUserById(userId);
        Event event = checkEventById(eventId);
        checkEventParticipantLimit(event);
        checkEventStateIsPENDING(event);

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {

            List<ParticipationRequestDto> confirmedAllRequests = new ArrayList<>();


            for (Long requestId : eventRequestStatusUpdateRequest.getRequestIds()) {

                ParticipationRequestDto requestDto = participationRequestRepository.findById(requestId).orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "Participation Request with id=" + requestId + " was not found",
                        LocalDateTime.now()));
                requestDto.setStatus(StatusRequest.CONFIRMED.toString());
                confirmedAllRequests.add(requestDto);
            }
            return new EventRequestStatusUpdateResult(confirmedAllRequests, Collections.emptyList());
        }

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        for (Long requestId : eventRequestStatusUpdateRequest.getRequestIds()) {

            ParticipationRequestDto participationRequest = participationRequestRepository.findById(requestId).orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                    "The required object was not found.",
                    "Participation Request with id=" + requestId + " was not found",
                    LocalDateTime.now()));


            // ТУТ ХЗ СО СТАТУСАМИ КАК
        }

        // ТУТ ХЗ СО СТАТУСАМИ КАК

        return null;
    }

    public List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states,
                                               List<Long> categories, LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd, int from, int size) {

        Pageable pageable = PageRequest.of((from / size), size);


        List<Event> eventList = eventRepository.findEventWhitParametersByAdmin(users, states, categories, rangeStart, rangeEnd, pageable)
                .orElse(Collections.emptyList());
        if (eventList.isEmpty()) {
            return Collections.emptyList();
        } else {
            return EventMapper.listEventToListEventFullDto(eventList);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public EventFullDto updateEventsByAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {

        Event event = checkEventById(eventId);

        if (updateEventAdminRequest.getEventDate() != null) {
            if (updateEventAdminRequest.getEventDate().plusHours(1).isBefore(LocalDateTime.now())) {
                throw new DataIntegrityViolationException("Date of update Events is too early");
            }
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }

        if (event.getState().equals(StateEvent.PUBLISHED.toString())) {
            throw new DataIntegrityViolationException("Cannot publish the event because " +
                    "it's not in the right state: PUBLISHED");
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateActionByAdmin.REJECT_EVENT.toString())
                    && event.getState().equals(StateEvent.PUBLISHED.toString())) {
                throw new DataIntegrityViolationException("Event already PUBLISHED");
            }
            if (updateEventAdminRequest.getStateAction().equals(StateActionByAdmin.PUBLISH_EVENT.toString())) {
                event.setState(StateEvent.PUBLISHED.toString());
            }
            if (updateEventAdminRequest.getStateAction().equals(StateActionByAdmin.REJECT_EVENT.toString())) {
                event.setState(StateEvent.CANCELED.toString());  // ?? не точно еще что такой статус
            }
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory()).orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                    "The required object was not found.",
                    "Category with id=" + updateEventAdminRequest.getCategory() + " was not found",
                    LocalDateTime.now())));
        }
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLon(updateEventAdminRequest.getLocation().getLon());
            event.setLat(updateEventAdminRequest.getLocation().getLat());
        }
        if (updateEventAdminRequest.isPaid()) {
            if (!event.isPaid()) {
                event.setPaid(updateEventAdminRequest.isPaid());
            }
        } else {
            if (event.isPaid()) {
                event.setPaid(updateEventAdminRequest.isPaid());
            }
        }

        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.isRequestModeration()) {
            if (!event.isRequestModeration()) {
                event.setPaid(updateEventAdminRequest.isRequestModeration());
            }
        } else {
            if (event.isRequestModeration()) {
                event.setPaid(updateEventAdminRequest.isRequestModeration());
            }
        }

        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        eventRepository.save(event);
        return EventMapper.eventToEventFullDto(event);
    }


    private void checkDataTime(LocalDateTime eventDate) {
        if (eventDate.plusHours(2).isBefore(LocalDateTime.now())) {
            throw new DataIntegrityViolationException("Field: eventDate. Error: должно содержать дату, которая еще " +
                    "не наступила. Value: " + eventDate);
        }
    }

    private UserDto checkUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                "User with id=" + userId + " was not found",
                LocalDateTime.now()));
    }

    private Event checkEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                "Event with id=" + eventId + " was not found",
                LocalDateTime.now()));
    }

    private EventFullDto checkEventInitiator(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            return EventMapper.eventToEventFullDto(event);
        } else {
            throw new ApiError(HttpStatus.NOT_FOUND.toString(),
                    "The required object was not found.",
                    "User with id=" + userId + " does not have event whit id= " + event.getId(),
                    LocalDateTime.now());
        }
    }

    private CategoryDto checkCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                "Category with id=" + categoryId + " was not found",
                LocalDateTime.now()));
    }

    private void checkEventParticipantLimit(Event event) {
        List<ParticipationRequestDto> listRequests = participationRequestRepository.findByEventId(event.getId());
        if (listRequests.size() >= event.getParticipantLimit()) {
            throw new DataIntegrityViolationException("In Event with id=" + event.getId() +
                    " no free places.");
        }
    }

    private void checkEventStateIsPENDING(Event event) {
        if (!event.getState().equals(StateEvent.PENDING.toString())) {
            throw new DataIntegrityViolationException("Event with id=" + event.getId() +
                    " state not PENDING");
        }
    }
}
