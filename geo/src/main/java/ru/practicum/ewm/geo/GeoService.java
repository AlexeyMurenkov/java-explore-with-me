package ru.practicum.ewm.geo;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.Location;

import java.util.Arrays;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeoService {

    private static final int MIN_RESPONSE_LENGTH = 10;
    GeoRepository geoRepository;
    GeoClient geoClient;

    public Location patch(Location location) {
        if (location == null) {
            return null;
        }

        if (location.getLat() != null && location.getLon() != null && (location.getName() == null)) {
            final Geo geo = geoRepository.findGeoByPos(location.getLat(), location.getLon());
            if (geo != null) {
                return LocationMapper.toLocation(geo);
            }
        }

        if (location.getName() != null && location.getName().length() >= MIN_RESPONSE_LENGTH &&
                (location.getLat() == null || location.getLon() == null)) {
            final Geo restoredGeo = geoRepository.findGeoByGeocode(location.getName());
            if (restoredGeo != null) {
                return LocationMapper.toLocation(restoredGeo);
            }
            final Geo downloadedGeo = geoClient.getLocation(location.getName());
            if (downloadedGeo != null) {
                return LocationMapper.toLocation(geoRepository.save(downloadedGeo));
            }
        }
        return location;
    }

    public Collection<Geo> getAllGeoByAdmin() {
        return geoRepository.findAll();
    }

    public void removeGeosByAdmin(Long[] ids) {
        geoRepository.deleteAllByIdInBatch(Arrays.asList(ids));
    }

    public void clearAllGeosByAdmin() {
        geoRepository.deleteAll();
    }
}
