package ru.practicum.mainserver.user;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.exception.ApiError;
import ru.practicum.mainserver.user.models.NewUserRequest;
import ru.practicum.mainserver.user.models.UserDto;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getUsersByAdmin(List<Long> ids, int from, int size) {

        Pageable pageable = PageRequest.of((from / size), size);

        List<UserDto> resultList = userRepository.findAllByOwner(ids, pageable);

        return resultList;
    }

    @Transactional(rollbackOn = Exception.class)
    public UserDto createUser(NewUserRequest newUserRequest) {
        List<UserDto> userWhitNewEmail = userRepository.findUserByEmail(newUserRequest.getEmail());
        if (!userWhitNewEmail.isEmpty()) {
            throw new DataIntegrityViolationException("User with email=" + newUserRequest.getEmail() + " already registered");
        }
        UserDto userToSave = UserMapper.newUserToUserDto(newUserRequest);
        userRepository.save(userToSave);
        return userToSave;
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteUserById(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                        "The required object was not found.",
                        "User with id=" + userId + " was not found",
                        LocalDateTime.now()));

        userRepository.deleteById(userId);
    }
}
