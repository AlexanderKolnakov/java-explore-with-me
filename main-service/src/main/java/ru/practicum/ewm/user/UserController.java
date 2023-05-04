package ru.practicum.ewm.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.models.NewUserRequest;
import ru.practicum.ewm.user.models.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<UserDto> getUsersByAdmin(
            @RequestParam List<Long> ids,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("ADMIN. Получен GET запрос на получение информации о пользователях");
        return userService.getUsersByAdmin(ids, from, size);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.debug("ADMIN. Получен POST запрос на создание нового пользователя.");
        return userService.createUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {
        log.debug("ADMIN. Получен DELETE запрос на удаление пользователя с id: {}.", userId);
        userService.deleteUserById(userId);
    }
}
