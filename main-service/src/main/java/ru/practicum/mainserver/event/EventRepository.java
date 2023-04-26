package ru.practicum.mainserver.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainserver.event.model.Event;
import ru.practicum.mainserver.event.model.EventFullDto;
import ru.practicum.mainserver.event.model.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

//    @Query("select e from Event e " +
//            "where e.initiator.id =? 1")
//    List<Event> findEventByCreatedUserId(Long userId, Pageable pageable);

    @Query("select EventShortDto(e.annotation, e.category, e.confirmedRequests, " +
            "e.eventDate, UserShortDto(e.initiator.id , e.initiator.name), " +
            "e.paid, e.title, e.views) from Event e " +
            "where e.initiator.id =? 1")
    Optional<List<EventShortDto>> findEventByCreatedUserId(Long userId, Pageable pageable);


    @Query("select e.id, e.annotation, e.category, e.confirmedRequests, e.createdOn, e.description, e.eventDate, " +
            "e.initiator, new ru.practicum.mainserver.event.model.Location(e.lat, e.lon), e.paid, e.participantLimit, " +
            "e. publishedOn, e.requestModeration, e.state, e.title, e.views " +
            "from Event e " +
            "where e.eventDate > ?4 and e.eventDate < ?5 and e.initiator.id in (:users) " +
            "and e.state in (:states) and e.category.id in (:categories)")
    Optional<List<EventFullDto>> findEventFullWhitParametersByAdmin(List<Long> users, List<String> states,
                                                                   List<Long> categories, LocalDateTime rangeStart,
                                                                   LocalDateTime rangeEnd, Pageable pageable);
}
