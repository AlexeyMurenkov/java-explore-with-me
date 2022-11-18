package ru.practicum.ewm.geo.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.Location;
import ru.practicum.ewm.geo.GeoService;

@RestController
@RequestMapping(path = "/geo")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicGeoController {

    GeoService geoService;

    @PatchMapping
    public Location patch(@RequestBody Location location) {
        return geoService.patch(location);
    }
}
