package ru.practicum.ewm.user;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.of(user.getId(), user.getEmail(), user.getName());
    }

    public static User fromUserDto(UserDto userDto) {
        return User.of(userDto.getId(), userDto.getEmail(), userDto.getName());
    }

    public static Collection<UserDto> toUsersDto(Collection<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
