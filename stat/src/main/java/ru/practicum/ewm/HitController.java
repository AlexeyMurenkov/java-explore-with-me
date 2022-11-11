package ru.practicum.ewm;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HitController {
    HitService hitService;

    @PostMapping("/hit")
    public void createHit(@RequestBody EndpointHit hit) {
        log.debug("POST to create hit {}", hit);
        hitService.create(hit);
    }

    @GetMapping("/stats")
    public Collection<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                              LocalDateTime start,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                              LocalDateTime end,
                                          @RequestParam(required = false) String[] uris,
                                          @RequestParam(defaultValue = "false") boolean uniq) {
        log.debug("GET stats for uris {} from {} to {}", uris, start, end);
        return hitService.getStat(start, end, uris, uniq);
    }
}
