package ru.practicum.ewm.compilation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.FromIndexPageRequest;
import ru.practicum.ewm.common.exception.NotFoundException;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationService {
    CompilationRepository compilationRepository;
    EventRepository eventRepository;

    private Compilation findCompilation(long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(String.format("Compilation with id=%s not found", compId))
        );
    }
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        final Compilation compilation = CompilationMapper.fromCompilationDto(compilationDto,
                eventRepository.findAllById(compilationDto.getEvents()));
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    public void removeCompilation(long compId) {
        compilationRepository.delete(findCompilation(compId));
    }

    public CompilationDto removeEvent(long compId, long eventId) {
        final Compilation compilation = findCompilation(compId);
        final Collection<Event> events = compilation.getEvents();
        final Event event = events.stream().filter(e -> e.getId() == eventId).findAny().orElseThrow(
                () -> new NotFoundException(String.format("Event id=%s not found in compilation id=%s", eventId,
                        compId))
        );

        compilation.getEvents().remove(event);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    public CompilationDto addEvent(long compId, long eventId) {
        final Compilation compilation = findCompilation(compId);
        final Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event id=%s not found", eventId))
        );

        compilation.getEvents().add(event);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    public CompilationDto pinCompilation(long compId) {
        final Compilation compilation = findCompilation(compId);
        compilation.setPinned(true);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    public CompilationDto unpinCompilation(long compId) {
        final Compilation compilation = findCompilation(compId);
        compilation.setPinned(false);
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    public Collection<CompilationDto> getCompilations(Optional<Boolean> pinned, int from, int size) {
        final Pageable pageable = FromIndexPageRequest.of(from, size);

        if (pinned.isPresent()) {
            return CompilationMapper.toCompilationDtos(compilationRepository.findAllByPinnedIs(pinned.get(), pageable)
                    .toList());
        }
        return  CompilationMapper.toCompilationDtos(compilationRepository.findAll(pageable).toList());
    }

    public CompilationDto getCompilation(long compId) {
        return CompilationMapper.toCompilationDto(findCompilation(compId));
    }
}
