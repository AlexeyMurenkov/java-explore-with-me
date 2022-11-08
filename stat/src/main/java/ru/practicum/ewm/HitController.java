package ru.practicum.ewm;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HitController {
    HitService hitService;

    @PostMapping("/hit")
    public void createHit(@RequestBody EndpointHit hit) {
        hitService.create(hit);
    }

    @GetMapping("/stats")
    public Collection<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                              LocalDateTime start,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                              LocalDateTime end,
                                          @RequestParam(required = false) String[] uris,
                                          @RequestParam(defaultValue = "false") boolean uniq) {
        return hitService.getStat(start, end, uris, uniq);
    }
}
