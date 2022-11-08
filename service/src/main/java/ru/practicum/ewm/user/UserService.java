package ru.practicum.ewm.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.FromIndexPageRequest;
import ru.practicum.ewm.common.exception.NotFoundException;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    public UserDto create(UserDto userDto) {
        final User user = UserMapper.fromUserDto(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    public void remove(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id=%s was not found.", userId));
        }

        userRepository.deleteById(userId);
    }

    public Collection<UserDto> getByIds(Collection<Long> ids, int from, int size) {
        final Pageable pageable = FromIndexPageRequest.of(from, size);
        return UserMapper.toUsersDto(userRepository.findAllByIdIn(ids, pageable));
    }

    public Collection<UserDto> getAll(int from, int size) {
        final Pageable pageable = FromIndexPageRequest.of(from, size);
        return UserMapper.toUsersDto(userRepository.findAll(pageable).toList());
    }
}
