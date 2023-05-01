package ru.practicum.ewm.participationReques;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.enums.StateActionByAdmin;
import ru.practicum.ewm.event.enums.StateEvent;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.participationReques.enums.StatusRequest;
import ru.practicum.ewm.participationReques.model.ParticipationRequestDto;
import ru.practicum.ewm.user.UserRepository;
import ru.practicum.ewm.user.models.UserDto;

import javax.persistence.EntityNotFoundException;
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

        ParticipationRequestDto participationRequest = new ParticipationRequestDto();

        if (!event.isRequestModeration()) {
            checkEventParticipantLimit(event);
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
        participationRequest.setStatus(StateEvent.CANCELED.toString());
        return participationRequestRepository.save(participationRequest);
    }

    private UserDto checkUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + userId + " was not found"));
    }

    private Event checkEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " was not found"));
    }

    private void checkEventInitiator(Event event, Long userId) {
        if (event.getInitiator().getId().equals(userId)) {
            throw new DataIntegrityViolationException("User with id=" + userId +
                    " is initiator event whit id= " + event.getId());
        }
    }

    private void checkEventState(Event event) {
        if (!(event.getState().equals(StateEvent.PUBLISHED.toString())
                || event.getState().equals(StateActionByAdmin.PUBLISH_EVENT.toString()))) {
            throw new DataIntegrityViolationException("Event with id=" + event.getId() +
                    " not published");
        }
    }

    private void checkEventByUserIsCreated(Long userId, Long eventId) {
        if (!participationRequestRepository.findByUserAndEventId(userId, eventId).orElse(Collections.emptyList()).isEmpty()) {
            throw new DataIntegrityViolationException("Event with id=" + eventId +
                    " already created by user with id=" + userId);
        }
    }

    private void checkEventParticipantLimit(Event event) {
        List<ParticipationRequestDto> listRequests = participationRequestRepository.findByEventId(event.getId());
        if (listRequests.size() >= event.getParticipantLimit()) {
            throw new DataIntegrityViolationException("Event is Full");
        }
        ;
    }

    private ParticipationRequestDto checkParticipationRequest(Long requestId) {
        return participationRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("ParticipationRequest with id=" + requestId +
                        " was not found"));
    }
}
