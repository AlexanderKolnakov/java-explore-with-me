package ru.practicum.mainserver.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.mainserver.category.CategoryRepository;
import ru.practicum.mainserver.category.CategoryService;
import ru.practicum.mainserver.category.models.CategoryDto;
import ru.practicum.mainserver.event.enums.SortEvent;
import ru.practicum.mainserver.event.enums.StateActionByAdmin;
import ru.practicum.mainserver.event.enums.StateActionByUser;
import ru.practicum.mainserver.event.enums.StateEvent;
import ru.practicum.mainserver.event.model.*;
import ru.practicum.mainserver.participationReques.ParticipationRequestRepository;
import ru.practicum.mainserver.participationReques.enums.StatusRequest;
import ru.practicum.mainserver.participationReques.model.ParticipationRequestDto;
import ru.practicum.mainserver.user.UserRepository;
import ru.practicum.mainserver.user.models.UserDto;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {

    //    private final StatsClient statsClient;
    private final EventRepository eventRepository;

    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    public List<EventShortDto> getEvents(String text, Long categories, Boolean paid, LocalDateTime rangeStart,
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
        checkEventById(id);


        // НУЖНО ПОДКЛЮЧИТЬ СЕРВИС СТАТИСТИКИ (что просматривали ивент)


        EndpointHitDto e = EndpointHitDto.builder()
                .app("ewm-main-service")
                .ip("XZ IP")
                .uri("XZ URI")
                .timestamp(LocalDateTime.now())
                .build();

//        statsClient.hit(e);


        return EventMapper.eventToEventFullDto(eventRepository
                .findPublishedEventById(id, StateEvent.PUBLISHED.toString()));

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
        createdEvent.setPaid(newEventDto.isPaid());

        eventRepository.save(createdEvent);
        return EventMapper.eventToEventFullDto(createdEvent);
    }

    public EventFullDto getFullEventsByUserId(Long userId, Long eventId) {
        checkUserById(userId);
        Event event = checkEventById(eventId);
        return checkEventInitiator(event, userId);
    }

    @Transactional(rollbackOn = Exception.class)
    public EventFullDto updateFullEventsByUserId(UpdateEventUserRequest updateEventUserRequest,
                                                 Long userId, Long eventId) {

        checkUserById(userId);
        Event event = checkEventById(eventId);
        if (updateEventUserRequest.getEventDate() != null) {
            checkDataTime(updateEventUserRequest.getEventDate());
        }

        if (updateEventUserRequest.getCategory() != null) {
            CategoryDto category = checkCategoryById(updateEventUserRequest.getCategory());
            event.setCategory(category);
        }
        if (event.getState().equals(StateEvent.PENDING.toString())
                || event.getState().equals(StateEvent.CANCELED.toString())) {

            if (updateEventUserRequest.getAnnotation() != null) {
                event.setAnnotation(updateEventUserRequest.getAnnotation());
            }
            if (updateEventUserRequest.getDescription() != null) {
                event.setDescription(updateEventUserRequest.getDescription());
            }
            if (updateEventUserRequest.getEventDate() != null) {
                event.setEventDate(updateEventUserRequest.getEventDate());
            }
            if (updateEventUserRequest.getLocation() != null) {
                event.setLat(updateEventUserRequest.getLocation().getLat());
                event.setLon(updateEventUserRequest.getLocation().getLon());
            }
            if (updateEventUserRequest.getParticipantLimit() != null) {
                event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
            }
            if (updateEventUserRequest.getStateAction() != null) {
                if (updateEventUserRequest.getStateAction()
                        .equals(StateActionByUser.CANCEL_REVIEW.toString())) {
                    event.setState(StateEvent.CANCELED.toString());
                }
                if (updateEventUserRequest.getStateAction()
                        .equals(StateActionByUser.SEND_TO_REVIEW.toString())) {
                    event.setState(StateEvent.PENDING.toString());
                }
            }
            if (updateEventUserRequest.getTitle() != null) {
                event.setTitle(updateEventUserRequest.getTitle());
            }
            if (updateEventUserRequest.getPaid() != null) {
                event.setPaid(updateEventUserRequest.getPaid());
            }

            if (updateEventUserRequest.getRequestModeration() != null) {
                event.setRequestModeration(updateEventUserRequest.getRequestModeration());
            }
            eventRepository.save(event);
            return EventMapper.eventToEventFullDto(event);
        } else {
            throw new DataIntegrityViolationException("Only pending or canceled events can be changed");
        }
    }

    public List<ParticipationRequestDto> getParticipationRequestByUserId(Long userId, Long eventId) {

        Event event = eventRepository.findById(eventId).get();
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            return Collections.emptyList();
        }
        return participationRequestRepository.findByEventId(eventId);
    }

    @Transactional(rollbackOn = Exception.class)
    public EventRequestStatusUpdateResult updateEventRequestStatusByUserId(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                                           Long userId, Long eventId) {

        checkUserById(userId);
        Event event = checkEventById(eventId);
        checkEventParticipantLimit(event);

        Long confirmedRequests = event.getConfirmedRequests();
        Long limitRequests = event.getParticipantLimit();

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            List<ParticipationRequestDto> resultList = new ArrayList<>();

            for (Long requestId : eventRequestStatusUpdateRequest.getRequestIds()) {

                ParticipationRequestDto requestDto = participationRequestRepository
                        .findById(requestId).orElseThrow(() -> new EntityNotFoundException("Participation Request with id=" +
                                requestId + " was not found"));
                requestDto.setStatus(StatusRequest.CONFIRMED.toString());  // мб тут поправить
                participationRequestRepository.save(requestDto);
                confirmedRequests++;
                resultList.add(requestDto);
            }
            event.setConfirmedRequests(confirmedRequests);
            eventRepository.save(event);
            return new EventRequestStatusUpdateResult(resultList, Collections.emptyList());
        }
        List<ParticipationRequestDto> confirmedtList = new ArrayList<>();
        List<ParticipationRequestDto> rejectedltList = new ArrayList<>();

        for (Long requestId : eventRequestStatusUpdateRequest.getRequestIds()) {

            ParticipationRequestDto participationRequest = participationRequestRepository
                    .findById(requestId)
                    .orElseThrow(() -> new EntityNotFoundException("Participation Request with id=" + requestId +
                            " was not found"));
            participationRequest.setStatus(eventRequestStatusUpdateRequest.getStatus());  // мб тут поправить
            participationRequestRepository.save(participationRequest);
            if (eventRequestStatusUpdateRequest.getStatus().equals(StatusRequest.CONFIRMED.toString())) {
                confirmedtList.add(participationRequest);
                confirmedRequests++;
                if (confirmedRequests >= limitRequests) {
                    eventRequestStatusUpdateRequest.setStatus(StatusRequest.REJECTED.toString());
                }
            } else {
                rejectedltList.add(participationRequest);
            }
        }
        event.setConfirmedRequests(confirmedRequests);
        eventRepository.save(event);
        return new EventRequestStatusUpdateResult(confirmedtList, rejectedltList);
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
        if (event.getState().equals(StateEvent.CANCELED.toString())) {
            throw new DataIntegrityViolationException("Cannot publish the event because " +
                    "it's not in the right state: CANCELED");
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
                event.setState(StateEvent.CANCELED.toString());
            }
        }
        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(updateEventAdminRequest.getCategory())
                    .orElseThrow(() -> new EntityNotFoundException("Category with id=" +
                            updateEventAdminRequest.getCategory() + " was not found")));
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
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
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
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + userId + " was not found"));
    }

    private Event checkEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " was not found"));
    }

    private EventFullDto checkEventInitiator(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            return EventMapper.eventToEventFullDto(event);
        } else {
            throw new EntityNotFoundException("User with id=" + userId +
                    " does not have event whit id= " + event.getId());
        }
    }

    private CategoryDto checkCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category with id=" + categoryId + " was not found"));
    }

    private void checkEventParticipantLimit(Event event) {
        List<ParticipationRequestDto> listRequests = participationRequestRepository.findByEventId(event.getId())
                .stream().filter(e -> e.getStatus()
                        .equals(StatusRequest.CONFIRMED.toString())).collect(Collectors.toList());
        if (listRequests.size() >= event.getParticipantLimit()) {
            throw new DataIntegrityViolationException("In Event with id=" + event.getId() +
                    " no free places.");
        }
    }
}
