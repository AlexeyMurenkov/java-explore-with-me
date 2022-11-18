package ru.practicum.ewm.geo;

import ru.practicum.ewm.dto.Location;

public class LocationMapper {
    public static Location toLocation(Geo geo) {
        return Location.of(
            geo.getName(),
            geo.getPointLat(),
            geo.getPointLon()
        );
    }
}
