package ru.practicum.ewm.participationReques;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.participationReques.model.ParticipationRequestDto;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequestDto, Long> {


    @Query("select p from ParticipationRequestDto p " +
            "where p.requester =? 1 and p.event =? 2 ")
    Optional<List<ParticipationRequestDto>> findByUserAndEventId(Long userId, Long eventId);


    @Query("select p from ParticipationRequestDto p " +
            "where p.event =? 1 ")
    List<ParticipationRequestDto> findByEventId(Long id);

    @Query("select p from ParticipationRequestDto p " +
            "where p.requester =? 1 ")
    List<ParticipationRequestDto> findByUserId(Long userId);
}
