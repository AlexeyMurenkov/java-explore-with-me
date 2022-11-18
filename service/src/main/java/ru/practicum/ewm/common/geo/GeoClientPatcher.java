package ru.practicum.ewm.common.geo;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.dto.Location;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.dto.EventGetDto;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeoClientPatcher {

    GeoClient geoClient;

    public EventGetDto patchEventGetDto(EventGetDto eventGetDto) {
        if (eventGetDto.getLocation() == null) return eventGetDto;
        final Location location = geoClient.patch(eventGetDto.getLocation());
        if (location.equals(eventGetDto.getLocation())) return eventGetDto;
        return eventGetDto.withLocation(location);
    }

    public EventDto patchEventDto(EventDto eventDto) {
        if (eventDto.getLocation() == null) return eventDto;
        final Location location = geoClient.patch(eventDto.getLocation());
        if (location.equals(eventDto.getLocation())) return eventDto;
        return eventDto.withLocation(location);
    }

}
