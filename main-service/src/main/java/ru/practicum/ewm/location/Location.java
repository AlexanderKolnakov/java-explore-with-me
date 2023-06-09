package ru.practicum.ewm.location;


import lombok.*;


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
