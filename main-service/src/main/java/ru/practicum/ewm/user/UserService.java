package ru.practicum.ewm.user;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.user.models.NewUserRequest;
import ru.practicum.ewm.user.models.UserDto;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getUsersByAdmin(List<Long> ids, int from, int size) {

        Pageable pageable = PageRequest.of((from / size), size);
        return userRepository.findAllByOwner(ids, pageable);
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
                .orElseThrow(() -> new EntityNotFoundException("User with id=" + userId + " was not found"));
        userRepository.deleteById(userId);
    }
}
