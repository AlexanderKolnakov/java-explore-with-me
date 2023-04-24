package ru.practicum.mainserver.participationReques;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.participationReques.model.ParticipationRequestDto;

import java.util.List;

@Service
@AllArgsConstructor
public class ParticipationRequestService {
    public List<ParticipationRequestDto> getParticipationRequestByUserId(int userId) {
        return null;
    }

    public ParticipationRequestDto createParticipationRequest(int userId, int eventId) {
        return null;

    }

    public ParticipationRequestDto cancelParticipationRequestByUserId(int userId, int requestId) {
        return null;

    }
}
