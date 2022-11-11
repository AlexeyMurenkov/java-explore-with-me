package ru.practicum.ewm;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.EndpointHit;
import ru.practicum.ewm.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class HitService {
    HitRepository hitRepository;

    public void create(EndpointHit hit) {
        hitRepository.save(HitMapper.fromEndpointHit(hit));
    }

    public Collection<ViewStats> getStat(LocalDateTime start, LocalDateTime end, String[] uris, boolean uniq) {
        if (uniq && uris != null && uris.length > 0) {
            return hitRepository.findStatsDistIp(start, end, uris);
        } else if (!uniq && uris != null && uris.length > 0) {
            return hitRepository.findStats(start, end, uris);
        } else if (uniq) {
            return hitRepository.findStatsDistIp(start, end);
        } else {
            return hitRepository.findStats(start, end);
        }
    }
}
