package ru.practicum.ewm.location;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.clients.GeoClient;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventFullDto;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class LocationService {
    private final GeoClient geoClient;
    private final EventRepository eventRepository;

    private final LocationRepository locationRepository;

    public LocationInMap getLoc(Float lat, Float lon)  {
        return geoClient.getLocFromYandex(lat, lon);
    }

    public Map<LocationInMap, EventFullDto> setLoc(Long eventId) {
        Event event = checkEventById(eventId);

//        LocationInMap location = geoClient.getLocFromYandex(event.getLat(), event.getLon());
//        if (location.getText().isEmpty()) {
//            throw new DataIntegrityViolationException("Event with id=" + eventId + " not have a location");
//        }
//        locationRepository.save(location);
//        EventMapper.eventToEventFullDto(event);
//        Map<LocationInMap, EventFullDto> resultMap = new HashMap<>();
//        resultMap.put(location, EventMapper.eventToEventFullDto(event));
//        return resultMap;
        return null;
    }

    private Event checkEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " was not found"));
    }
}
