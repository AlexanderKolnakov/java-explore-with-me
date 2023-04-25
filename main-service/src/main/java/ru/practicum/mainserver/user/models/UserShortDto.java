package ru.practicum.mainserver.user.models;


import lombok.*;

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
