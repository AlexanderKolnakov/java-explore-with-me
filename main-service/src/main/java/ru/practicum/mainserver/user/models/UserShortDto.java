package ru.practicum.mainserver.user.models;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserShortDto {

    // Пользователь (краткая информация)

    private Long id;   // Идентификатор пользователя

    private String name;   // Имя пользователя
}
