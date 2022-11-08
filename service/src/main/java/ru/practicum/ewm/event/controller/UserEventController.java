package ru.practicum.ewm.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.validation.group.Create;
import ru.practicum.ewm.common.validation.group.Update;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.dto.EventGetDto;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.RequestDto;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class UserEventController {
    EventService eventService;

    @GetMapping("/{userId}/events")
    public Collection<EventGetDto> getEventsByUserId(@PathVariable @NotNull Long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsByUserId(userId, from, size);
    }

    @PatchMapping("/{userId}/events")
    public EventGetDto updateEvent(@PathVariable @NotNull Long userId,
                                   @RequestBody @Validated(Update.class) @NotNull EventDto eventDto) {
        return eventService.updateEvent(userId, eventDto);
    }

    @PostMapping("/{userId}/events")
    public EventGetDto createEvent(@PathVariable @NotNull Long userId,
                                   @RequestBody @Validated(Create.class) @NotNull EventDto eventDto) {
        return eventService.createEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventGetDto getEventByIdAndUserId(@PathVariable @NotNull Long eventId, @PathVariable @NotNull Long userId) {
        return eventService.getEventByIdAndUserId(eventId, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventGetDto cancelEventIdAndUserId(@PathVariable @NotNull Long eventId, @PathVariable @NotNull Long userId) {
        return eventService.cancelEventByIdAndUserId(eventId, userId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public Collection<RequestDto> getRequestsByUserIdAndEventId(@PathVariable @NotNull Long userId,
                                                                @PathVariable @NotNull Long eventId) {
        return eventService.getRequestsByUserIdAndEventId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequest(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long eventId,
                                     @PathVariable @NotNull Long reqId) {
        return eventService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequest(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long eventId,
                                     @PathVariable @NotNull Long reqId) {
        return eventService.rejectRequest(userId, eventId, reqId);
    }

    @GetMapping("/{userId}/requests")
    public Collection<RequestDto> getRequests(@PathVariable @NotNull Long userId) {
        return eventService.getRequests(userId);
    }

    @PostMapping("/{userId}/requests")
    public RequestDto createRequest(@PathVariable @NotNull Long userId, @RequestParam @NotNull Long eventId) {
        return eventService.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{reqId}/cancel")
    public RequestDto cancelRequest(@PathVariable @NotNull Long userId, @PathVariable @NotNull Long reqId) {
        return eventService.cancelRequest(userId, reqId);
    }
}
