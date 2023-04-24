package ru.practicum.mainserver.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.user.models.NewUserRequest;
import ru.practicum.mainserver.user.models.UserDto;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    public List<UserDto> getUsersByAdmin(List<Integer> ids, int from, int size) {
        return null;
    }

    public UserDto createUser(NewUserRequest newUserRequest) {
        return null;
    }

    public void deleteUserById(int userId) {
    }
}
