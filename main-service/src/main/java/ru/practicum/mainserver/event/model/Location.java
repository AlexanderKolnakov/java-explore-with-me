package ru.practicum.mainserver.event.model;


import lombok.*;
import org.springframework.stereotype.Component;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor

public class Location {

    // Широта и долгота места проведения события

    private float lat;   // Широта

    private float lon;   // Долгота
}
