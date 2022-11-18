package ru.practicum.ewm.geo.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.geo.Geo;
import ru.practicum.ewm.geo.GeoService;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping(path = "/admin/geo")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminGeoController {

    GeoService geoService;

    @GetMapping
    public Collection<Geo> getAll() {
        return geoService.getAllGeoByAdmin();
    }

    @DeleteMapping
    public void remove(@RequestParam @NotNull Long[] ids) {
        geoService.removeGeosByAdmin(ids);
    }

    @DeleteMapping("/clear")
    public void clear() {
        geoService.clearAllGeosByAdmin();
    }

}
