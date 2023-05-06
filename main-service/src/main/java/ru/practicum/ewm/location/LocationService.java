package ru.practicum.ewm.location;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.clients.GeoClient;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventFullDto;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class LocationService {
    private final GeoClient geoClient;
    private final EventRepository eventRepository;

    private final LocationRepository locationRepository;

    @Transactional(rollbackOn = Exception.class)
    public LocationInMap setLoc(Float lat, Float lon)  {
        return locationRepository.save(geoClient.getLocFromYandex(lat, lon));
    }

    public List<LocationInMap> getAllLocation() {
        return locationRepository.findAll();
    }

    private Event checkEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " was not found"));
    }

    public List<EventFullDto> getEventsByLocationText(String locationText, LocalDateTime rangeStart,
                                                      LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of((from / size), size);
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        List<Event> eventList = eventRepository.findEventByLocationText(locationText, rangeStart, rangeEnd, pageable);
        return EventMapper.listEventToListEventFullDto(eventList);
    }
}
