package ru.practicum.mainserver.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainserver.event.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e " +
            "where e.initiator.id =? 1")
    List<Event> findEventByCreatedUserId(Long userId, Pageable pageable);
}
