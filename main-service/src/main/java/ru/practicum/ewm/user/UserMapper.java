package ru.practicum.ewm.user;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.user.models.NewUserRequest;
import ru.practicum.ewm.user.models.UserDto;

@Component
public class UserMapper {

    public static UserDto newUserToUserDto(NewUserRequest newUserRequest) {
        UserDto userDto = new UserDto();
        userDto.setName(newUserRequest.getName());
        userDto.setEmail(newUserRequest.getEmail());
        return userDto;
    }
}
