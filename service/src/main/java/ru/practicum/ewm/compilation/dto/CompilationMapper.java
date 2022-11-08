package ru.practicum.ewm.compilation.dto;

import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.event.dto.EventMapper;
import ru.practicum.ewm.event.model.Event;

import java.util.Collection;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation fromCompilationDto(NewCompilationDto compilationDto, Collection<Event> events) {
        return Compilation.of(null, compilationDto.getTitle(), compilationDto.isPinned(), events);
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.of(
                compilation.getId(),
                compilation.getTitle(),
                compilation.isPinned(),
                EventMapper.toEventShortDtos(compilation.getEvents())
        );
    }

    public static Collection<CompilationDto> toCompilationDtos(Collection<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }
}
