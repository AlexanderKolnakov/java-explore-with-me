package ru.practicum.mainserver.compilation;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainserver.compilation.models.CompilationDto;
import ru.practicum.mainserver.compilation.models.NewCompilationDto;
import ru.practicum.mainserver.compilation.models.UpdateCompilationRequest;
import ru.practicum.mainserver.event.model.EventShortDto;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping()
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilation(
            @RequestParam(required = false) boolean pinned,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Получен GET запрос на получение списка подборок событий, с флагом о закрепленности - " + pinned);
        return compilationService.getCompilation(pinned, from, size);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Получен GET запрос на получение подборок событий c id - " + compId);
        return compilationService.getCompilationById(compId);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("ADMIN. Получен POST запрос на создание подборки событий");
        return compilationService.createCompilation(newCompilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("ADMIN. Получен DELETE запрос на удаление подборки событий с id: {}.", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable Long compId, @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("ADMIN. Получен PATCH запрос на обновление информации о подборки событий с id: " + compId);
        return compilationService.updateCompilation(compId, updateCompilationRequest);
    }
}
