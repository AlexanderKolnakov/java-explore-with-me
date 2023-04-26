package ru.practicum.mainserver.participationReques;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.event.EventRepository;
import ru.practicum.mainserver.event.enums.StateEvent;
import ru.practicum.mainserver.event.model.Event;
import ru.practicum.mainserver.exception.ApiError;
import ru.practicum.mainserver.participationReques.enums.StatusRequest;
import ru.practicum.mainserver.participationReques.model.ParticipationRequestDto;
import ru.practicum.mainserver.user.UserRepository;
import ru.practicum.mainserver.user.models.UserDto;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public List<ParticipationRequestDto> getParticipationRequestByUserId(Long userId) {
        checkUserById(userId);

        return participationRequestRepository.findByUserId(userId);
    }

    @Transactional(rollbackOn = Exception.class)
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        checkUserById(userId);
        Event event = checkEventById(eventId);

        checkEventInitiator(event, userId);
        checkEventState(event);
        checkEventByUserIsCreated(userId, eventId);
        checkEventParticipantLimit(event);

        ParticipationRequestDto participationRequest = new ParticipationRequestDto();

        if(!event.isRequestModeration()) {
            participationRequest.setStatus(StatusRequest.CONFIRMED.toString());
        } else {
            participationRequest.setStatus(StatusRequest.PENDING.toString());
        }
        participationRequest.setCreated(LocalDateTime.now());
        participationRequest.setEvent(eventId);
        participationRequest.setRequester(userId);

        return participationRequestRepository.save(participationRequest);
    }


    @Transactional(rollbackOn = Exception.class)
    public ParticipationRequestDto cancelParticipationRequestByUserId(Long userId, Long requestId) {
        checkUserById(userId);

        ParticipationRequestDto participationRequest = checkParticipationRequest(requestId);
        participationRequest.setStatus(StatusRequest.CONFIRMED.toString());
        return participationRequestRepository.save(participationRequest);
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

    private void checkEventInitiator(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            throw new DataIntegrityViolationException("User with id=" + userId +
                    " is initiator event whit id= " + event.getId());
        }
    }

    private void checkEventState(Event event) {
        if (!event.getState().equals(StateEvent.PUBLISHED.toString())) {
            throw new DataIntegrityViolationException("Event with id=" + event.getId() +
                    " not published");
        }
    }

    private void checkEventByUserIsCreated(Long userId, Long eventId) {
        if (participationRequestRepository.findByUserAndEventId(userId, eventId).orElse(Collections.emptyList()).isEmpty()) {
            throw new DataIntegrityViolationException("Event with id=" + eventId +
                    " already created by user with id=" + userId);
        }
    }

    private void checkEventParticipantLimit(Event event) {
        List<ParticipationRequestDto> listRequests = participationRequestRepository.findByEventId(event.getId());
        if (listRequests.size() >= event.getParticipantLimit()) {
            throw new DataIntegrityViolationException("In Event with id=" + event.getId() +
                    " no free places." );
        }
    }

    private ParticipationRequestDto checkParticipationRequest(Long requestId) {
        return participationRequestRepository.findById(requestId).orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                "ParticipationRequest with id=" + requestId + " was not found",
                LocalDateTime.now()));
    }
}
