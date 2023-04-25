package ru.practicum.mainserver.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainserver.user.models.UserDto;

import java.util.List;

public interface UserRepository extends JpaRepository<UserDto, Long> {

    @Query("select u from UserDto u " +
            "where u.id in (:userId)")
    List<UserDto> findAllByOwner(List<Long> userId, Pageable pageable);

    @Query("select u from UserDto u " +
            "where u.email =? 1")
    List<UserDto> findUserByEmail(String email);

}
