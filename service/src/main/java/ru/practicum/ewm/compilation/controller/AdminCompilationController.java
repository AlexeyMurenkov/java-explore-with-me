package ru.practicum.ewm.compilation.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.CompilationService;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class AdminCompilationController {

    CompilationService compilationService;

    @PostMapping
    public CompilationDto createCompilation(@RequestBody @NotNull @Valid NewCompilationDto compilationDto) {
        return compilationService.createCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public void removeCompilation(@PathVariable @NotNull Long compId) {
        compilationService.removeCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public CompilationDto removeEvent(@PathVariable @NotNull Long compId, @PathVariable Long eventId) {
        return compilationService.removeEvent(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public CompilationDto addEvent(@PathVariable @NotNull Long compId, @PathVariable @NotNull Long eventId) {
        return compilationService.addEvent(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public CompilationDto pinCompilation(@PathVariable @NotNull Long compId) {
        return compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public CompilationDto unpinCompilation(@PathVariable @NotNull Long compId) {
        return compilationService.pinCompilation(compId);
    }
}
