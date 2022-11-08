package ru.practicum.ewm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(value =
            "select new ru.practicum.ewm.dto.ViewStats(h.app, h.uri, count(h.ip)) " +
            "from Hit h " +
            "   where h.uri in (:uris) and h.timestamp between :start and :stop " +
            "group by h.app, h.uri")

    List<ViewStats> findStats(LocalDateTime start, LocalDateTime stop, String[] uris);

    @Query(value =
            "select new ru.practicum.ewm.dto.ViewStats(h.app, h.uri, count(h.ip)) " +
                    "from Hit h " +
                    "   where h.timestamp between :start and :stop " +
                    "group by h.app, h.uri")

    List<ViewStats> findStats(LocalDateTime start, LocalDateTime stop);

    @Query(value =
            "select new ru.practicum.ewm.dto.ViewStats(h.app, h.uri, count(distinct h.ip)) " +
                    "from Hit h " +
                    "   where h.uri in (:uris) and h.timestamp between :start and :stop " +
                    "group by h.app, h.uri")
    List<ViewStats> findStatsDistIp(LocalDateTime start, LocalDateTime stop, String[] uris);

    @Query(value =
            "select new ru.practicum.ewm.dto.ViewStats(h.app, h.uri, count(distinct h.ip)) " +
                    "from Hit h " +
                    "   where h.timestamp between :start and :stop " +
                    "group by h.app, h.uri")
    List<ViewStats> findStatsDistIp(LocalDateTime start, LocalDateTime stop);

}
