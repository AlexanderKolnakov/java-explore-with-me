package ru.practicum.ewm.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e " +
            "where e.initiator.id =? 1")
    Optional<List<Event>> findEventByCreatedUserId(Long userId, Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate between :rangeStart and :rangeEnd " +
            "and e.initiator.id in (:users) " +
            "and e.state in (:states) " +
            "and e.category.id in (:categories)")
    Optional<List<Event>> findEventWhitParametersByAdmin(@Param("users") List<Long> users,
                                                         @Param("states") List<String> states,
                                                         @Param("categories") List<Long> categories,
                                                         @Param("rangeStart") LocalDateTime rangeStart,
                                                         @Param("rangeEnd") LocalDateTime rangeEnd,
                                                         Pageable pageable);

    @Query("select e from Event e " +
            "where e.eventDate between :rangeStart and :rangeEnd " +
            "and  e.state in :state " +
            "and e.category.id in :categories " +
            "and e.paid in :paid " +
            "and upper(e.annotation) like  upper(concat('%', :text, '%')) " +
            " or upper(e.description) like upper(concat('%', :text, '%'))")
    Optional<List<Event>> findPublicEventWhitParametersByUser(@Param("rangeStart") LocalDateTime rangeStart,
                                                              @Param("rangeEnd") LocalDateTime rangeEnd,
                                                              @Param("state") String state,
                                                              @Param("categories") Long categories,
                                                              @Param("paid") Boolean paid,
                                                              @Param("text") String text,
                                                              Pageable pageable);


    @Query("select e from Event e " +
            "where e.eventDate between :rangeStart and :rangeEnd " +
            "and  e.state in :state " +
            "and e.category.id in :categories " +
            "and e.paid in :paid " +
            "and upper(e.annotation) like  upper(concat('%', :text, '%')) " +
            " or upper(e.description) like upper(concat('%', :text, '%'))" +
            "and e.participantLimit > e.confirmedRequests")
    Optional<List<Event>> findPublicEventWhitParametersByUserWhereEventAvailable(@Param("rangeStart") LocalDateTime rangeStart,
                                                                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                                                                 @Param("state") String state,
                                                                                 @Param("categories") Long categories,
                                                                                 @Param("paid") Boolean paid,
                                                                                 @Param("text") String text,
                                                                                 Pageable pageable);

    @Query("select e from Event e " +
            "where e.id =? 1 " +
            "and e.state =? 2 ")
    Event findPublishedEventById(Long id, String state);

    @Query("select e from Event e " +
            "where e.category.id =? 1")
    Optional<List<Event>> findByCategory(Long catId);

    @Query("select e from Event e " +
            "where e.id in (:events) ")
    Optional<List<Event>> findByListId(List<Long> events);
}
