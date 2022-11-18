package ru.practicum.ewm.geo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "geos", schema = "public")
@Getter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Geo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String request;
    String name;
    @Column(name = "point_lat")
    Float pointLat;
    @Column(name = "point_lon")
    Float pointLon;
    @Column(name = "lower_corner_lat")
    Float lowerCornerLat;
    @Column(name = "lower_corner_lon")
    Float lowerCornerLon;
    @Column(name = "upper_corner_lat")
    Float upperCornerLat;
    @Column(name = "upper_corner_lon")
    Float upperCornerLon;
}
