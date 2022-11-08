package ru.practicum.ewm.event.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.common.StatsClient;
import ru.practicum.ewm.event.EventService;
import ru.practicum.ewm.event.EventSort;
import ru.practicum.ewm.event.dto.EventGetDto;
import ru.practicum.ewm.dto.EndpointHit;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicEventController {

    EventService eventService;
    StatsClient statsClient;

    @GetMapping
    public Collection<EventGetDto> getEvents(@RequestParam Optional<String> text, @RequestParam Long[] categories,
                                             @RequestParam Optional<Boolean> paid,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                 Optional<LocalDateTime> rangeStart,
                                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                 Optional<LocalDateTime> rangeEnd,
                                             @RequestParam boolean onlyAvailable, @RequestParam Optional<EventSort> sort,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size, HttpServletRequest request) {
        statsClient.save(EndpointHit.of("ewm", request.getRequestURI(), request.getRemoteAddr()));
        return eventService.getEventsByPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from,
                size);
    }

    @GetMapping("/{eid}")
    public EventGetDto getEvent(@PathVariable @NotNull Long eid, HttpServletRequest request) {
        statsClient.save(EndpointHit.of("ewm", request.getRequestURI(), request.getRemoteAddr()));
        return eventService.getEventByPublic(eid);
    }
}
