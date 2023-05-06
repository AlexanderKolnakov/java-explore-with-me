package ru.practicum.ewm.location;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.clients.GeoClient;

@Component
@AllArgsConstructor

public class LocationMapper {

    private final GeoClient geoClient;


//    public static LocationInMap locationToLocationInMap(Location location) {
//
//
//
//    }
}
