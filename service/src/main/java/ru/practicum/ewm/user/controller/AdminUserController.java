package ru.practicum.ewm.user.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.user.UserDto;
import ru.practicum.ewm.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class AdminUserController {

    UserService userService;

    @PostMapping
    public UserDto create(@RequestBody @Valid @NotNull UserDto userDto) {
        return userService.create(userDto);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable Long userId) {
        userService.remove(userId);
    }

    @GetMapping
    public Collection<UserDto> getByIds(@RequestParam Optional<Collection<Long>> ids, @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return ids
                .map(i -> userService.getByIds(i, from, size))
                .orElse(userService.getAll(from, size));
    }
}
