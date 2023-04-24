package ru.practicum.mainserver.user.models;


import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
//@Table(name = "HITS")
public class NewUserRequest {

    // Данные нового пользователя

    private String email;

    private String name;
}
