package ru.practicum.ewm.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.UserDto;
import ru.practicum.ewm.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class AdminUserController {

    UserService userService;

    @PostMapping
    public UserDto create(@RequestBody @Valid @NotNull UserDto userDto) {
        log.debug("POST to create user {}", userDto);
        return userService.create(userDto);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable Long userId) {
        log.debug("DELETE to remove user for userId={}", userId);
        userService.remove(userId);
    }

    @GetMapping
    public Collection<UserDto> getByIds(@RequestParam Optional<Collection<Long>> ids, @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.debug("GET users by admin from={} size={}", from, size);
        return ids
                .map(i -> userService.getByIds(i, from, size))
                .orElse(userService.getAll(from, size));
    }
}
