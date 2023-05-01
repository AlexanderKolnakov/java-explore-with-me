package ru.practicum.ewm.user.models;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    // Данные нового пользователя

    @Email
    private String email;

    @NotBlank(message = "Field: name. Error: must not be blank. Value: {name}")
    @NotNull(message = "Field: name. Error: must not be blank. Value: {name}")
    private String name;
}
