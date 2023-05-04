package ru.practicum.ewm.user.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "USERS")
public class UserDto {

    // Пользователь

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // Идентификатор пользователя

    @Column(name = "USER_NAME")
    private String name;   // Имя пользователя

    @Column(name = "USER_EMAIL")
    private String email;   // Почтовый адрес пользователя
}
