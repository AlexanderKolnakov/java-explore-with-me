package ru.practicum.ewm.location;


import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class LocationInMap {

    // Информация о месте проведения события

    private float lat;   // Широта

    private float lon;   // Долгота

    private String countryCode;   // Код страны

    private String text;   // Описание места события с Яндекс.Карт

}
