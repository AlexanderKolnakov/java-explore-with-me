package ru.practicum.ewm.location;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.EventFullDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping()
public class LocationController {
    private final LocationService locationService;

    @PostMapping("/admin/location")
    @ResponseStatus(HttpStatus.CREATED)
    public LocationInMap setLocation(
            @RequestParam Float lat,
            @RequestParam Float lon) {
        log.info("ADMIN. Получен POST запрос на создание новой локации по координатам");
        return locationService.setLoc(lat, lon);
    }

    @GetMapping("/admin/location")
    public List<LocationInMap> getAllLocation() {
        log.info("ADMIN. Получен GET запрос на получении информации о всех созданных локациях");
        return locationService.getAllLocation();
    }

    @GetMapping("/events/location")
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) String locationText,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        log.info("Получен GET запрос на получение списка событий по параметрам, включая локацию");
        return locationService.getEventsByLocationText(locationText, rangeStart, rangeEnd, from, size);
    }
}
