package ru.practicum.mainserver.participationReques;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.participationReques.model.ParticipationRequestDto;

import java.util.List;

@Service
@AllArgsConstructor
public class ParticipationRequestService {
    public List<ParticipationRequestDto> getParticipationRequestByUserId(Long userId) {
        return null;
    }

    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        return null;

    }

    public ParticipationRequestDto cancelParticipationRequestByUserId(Long userId, Long requestId) {
        return null;

    }
}
