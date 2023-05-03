package ru.practicum.ewm.event;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.StatsClient;
import ru.practicum.ewm.category.CategoryRepository;
import ru.practicum.ewm.category.CategoryService;
import ru.practicum.ewm.category.models.CategoryDto;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.event.enums.SortEvent;
import ru.practicum.ewm.event.enums.StateActionByAdmin;
import ru.practicum.ewm.event.enums.StateActionByUser;
import ru.practicum.ewm.event.enums.StateEvent;
import ru.practicum.ewm.event.model.*;
import ru.practicum.ewm.participationReques.ParticipationRequestRepository;
import ru.practicum.ewm.participationReques.enums.StatusRequest;
import ru.practicum.ewm.participationReques.model.ParticipationRequestDto;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.models.UserDto;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {

    private final StatsClient statsClient;
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    public List<EventShortDto> getEvents(String text, Long categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size,
                                         String ip, String url) {

        Pageable pageable = PageRequest.of((from / size), size);
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        List<Event> eventList;

        if (onlyAvailable) {

            eventList = eventRepository
                    .findPublicEventWhitParametersByUserWhereEventAvailable(rangeStart, rangeEnd,
                            StateEvent.PUBLISHED.toString(), categories, paid, text, pageable)
                    .orElse(Collections.emptyList());
        } else {
            eventList = eventRepository
                    .findPublicEventWhitParametersByUser(rangeStart, rangeEnd,
                            StateEvent.PUBLISHED.toString(), categories, paid, text, pageable)
                    .orElse(Collections.emptyList());
        }
        if (sort.equals(SortEvent.EVENT_DATE.toString())) {
            eventList.sort(Comparator.comparing(Event::getEventDate).reversed());
        }
        if (sort.equals(SortEvent.VIEWS.toString())) {
            eventList.sort(Comparator.comparing(Event::getViews).reversed());
        }
        createHit(ip, url);
        List<Event> resultList = setViewToEvents(eventList);
        return EventMapper.listEventToListEventShortDto(resultList);
    }

    public EventFullDto getFullEventsById(Long id, String ip, String url) {
        checkEventById(id);
        createHit(ip, url);

        Event event = setViewToEvents(List.of(eventRepository
                .findPublishedEventById(id, StateEvent.PUBLISHED.toString()))).get(0);
        return EventMapper.eventToEventFullDto(event);
    }

    public List<EventShortDto> getEventsByUserId(Long userId, int from, int size) {
        Pageable pageable = PageRequest.of((from / size), size);

        List<Event> eventList = eventRepository.findEventByCreatedUserId(userId, pageable)
                .orElse(Collections.emptyList());
        if (eventList.isEmpty()) {
            return Collections.emptyList();
        } else {
            return EventMapper.listEventToListEventShortDto(setViewToEvents(eventList));
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
        return checkEventInitiator(setViewToEvents(List.of(event)).get(0), userId);
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
            setViewToEvents(List.of(event));
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

        List<Long> listParticipationRequestsId = new ArrayList<>(eventRequestStatusUpdateRequest.getRequestIds());
        List<ParticipationRequestDto> resultList = participationRequestRepository.findAllById(listParticipationRequestsId);

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {

            for (ParticipationRequestDto request : resultList) {
                request.setStatus(StatusRequest.CONFIRMED.toString());
                confirmedRequests++;
            }
            participationRequestRepository.saveAll(resultList);

            event.setConfirmedRequests(confirmedRequests);
            eventRepository.save(event);
            return new EventRequestStatusUpdateResult(resultList, Collections.emptyList());
        }

        List<ParticipationRequestDto> confirmedList = new ArrayList<>();
        List<ParticipationRequestDto> rejectedList = new ArrayList<>();

        for (ParticipationRequestDto request : resultList) {
            request.setStatus(eventRequestStatusUpdateRequest.getStatus());
            if (eventRequestStatusUpdateRequest.getStatus().equals(StatusRequest.CONFIRMED.toString())) {
                confirmedList.add(request);
                confirmedRequests++;
                if (confirmedRequests >= limitRequests) {
                    eventRequestStatusUpdateRequest.setStatus(StatusRequest.REJECTED.toString());
                }
            } else {
                rejectedList.add(request);
            }
        }
        participationRequestRepository.saveAll(resultList);

        event.setConfirmedRequests(confirmedRequests);
        eventRepository.save(event);
        return new EventRequestStatusUpdateResult(confirmedList, rejectedList);
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
            return EventMapper.listEventToListEventFullDto(setViewToEvents(eventList));
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
        setViewToEvents(List.of(event));
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

    private void createHit(String ip, String url) {
        String serviceName = "main-service";
        EndpointHitDto endpointHitDto = new EndpointHitDto(serviceName, url, ip, LocalDateTime.now());

        statsClient.hit(endpointHitDto);
    }

    private List<Event> setViewToEvents(List<Event> events) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<String> eventUris = events
                .stream()
                .map(e -> String.format("/events/%s", e.getId()))
                .collect(Collectors.toList());
        String start = LocalDateTime.now().minusYears(2).format(dateTimeFormatter);
        String end = LocalDateTime.now().plusYears(2).format(dateTimeFormatter);

        List<ViewStatsDto> viewList = statsClient.getStats(start, end, eventUris);
        Map<String, Long> eventViews = new HashMap<>();

        for (ViewStatsDto viewStat : viewList) {
            eventViews.put(viewStat.getUri(), viewStat.getHits());
        }

        return events.stream().peek(event -> {
            Long hits = eventViews.get(String.format("/events/%s", event.getId()));
            event.setViews(hits != null ? hits : 0);
        }).collect(Collectors.toList());
    }
}
