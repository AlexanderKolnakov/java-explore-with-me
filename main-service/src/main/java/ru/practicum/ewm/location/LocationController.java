package ru.practicum.ewm.location;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.model.EventFullDto;

import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping()
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/admin/location")
    public LocationInMap getLocation(
            @RequestParam Float lat,
            @RequestParam Float lon)  {
        log.info("ADMIN. Получен GET запрос на получении данные о геопозиции по координатам");
        return locationService.getLoc(lat, lon);
    }
    @PostMapping("/admin/events/location/{eventId}")
    public Map<LocationInMap, EventFullDto> setEventLocation(@PathVariable Long eventId)  {
        log.info("ADMIN. Получен POST запрос на добавление локации мероприятия с id=" + eventId);
        return locationService.setLoc(eventId);
    }
}
