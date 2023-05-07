package ru.practicum.ewm.location;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "LOCATION_IN_MAP")
public class LocationInMap {

    // Информация о месте проведения события

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private float lat;   // Широта

    private float lon;   // Долгота

    @Column(name = "COUNTRY_CODE")
    private String countryCode;   // Код страны

    @Column(name = "CITY")
    private String city;   // Город

    @Column(name = "LOCATION_TEXT")
    private String text;   // Описание места события с Яндекс.Карт

}
