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
@Entity
@AllArgsConstructor
//@Table(name = "HITS")
public class UserDto {

    // Пользователь

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // Идентификатор пользователя

    private String name;   // Имя пользователя

    private String email;   // Почтовый адрес пользователя
}
