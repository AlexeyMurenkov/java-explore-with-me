package ru.practicum.ewm.geo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GeoRepository extends JpaRepository<Geo, Long> {
    @Query(value =
            "select g from Geo g where (:lat between g.lowerCornerLat and g.upperCornerLat) and " +
                    "(:lon between g.lowerCornerLon and g.upperCornerLon)"
    )
    Geo findGeoByPos(Float lat, Float lon);

    @Query(value =
            "select g from Geo g where lower(:geocode)=lower(g.request) or " +
                    "lower(:geocode) like lower(concat('%', g.name, '%'))"
    )
    Geo findGeoByGeocode(String geocode);
}
