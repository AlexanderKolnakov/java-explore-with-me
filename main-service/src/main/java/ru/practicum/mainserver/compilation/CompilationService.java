package ru.practicum.mainserver.compilation;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.compilation.models.*;
import ru.practicum.mainserver.event.EventMapper;
import ru.practicum.mainserver.event.EventRepository;
import ru.practicum.mainserver.event.model.Event;
import ru.practicum.mainserver.event.model.EventShortDto;
import ru.practicum.mainserver.exception.ApiError;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationEventRepository compilationEventRepository;

    public List<CompilationDto> getCompilation(boolean pinned, int from, int size) {
        return null;
    }

    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                "Compilation with id=" + compId + " was not found",
                LocalDateTime.now()));
        List<CompilationEvent> compilationEvent = compilationEventRepository
                .findByCompilationId(compilation.getId());

        List<EventShortDto> events = new ArrayList<>();

        for (CompilationEvent comEvent : compilationEvent) {
            events.add(EventMapper.eventToEventShortDto(eventRepository
                    .findById(comEvent.getEventId()).orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                    "The required object was not found.",
                    "Event with id=" + compId + " was not found",
                    LocalDateTime.now()))));
        }

        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.isPinned());
        compilationDto.setEvents(events);

        return compilationDto;
    }

    @Transactional(rollbackOn = Exception.class)
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {

        List<Event> eventList = checkEvents(newCompilationDto.getEvents());
        Compilation compilation = new Compilation();

        compilation.setPinned(newCompilationDto.isPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        Compilation savedCompilation = compilationRepository.save(compilation);

        for (Long eventId : newCompilationDto.getEvents()) {
            CompilationEvent compilationEvent = new CompilationEvent();
            compilationEvent.setEventId(eventId);
            compilationEvent.setCompilationId(savedCompilation.getId());

            compilationEventRepository.save(compilationEvent);
        }
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setEvents(EventMapper.listEventToListEventShortDto(eventList));
        compilationDto.setId(savedCompilation.getId());
        compilationDto.setPinned(savedCompilation.isPinned());
        compilationDto.setTitle(savedCompilation.getTitle());

        return compilationDto;
    }


    @Transactional(rollbackOn = Exception.class)
    public void deleteCompilation(Long compId) {
    }

    @Transactional(rollbackOn = Exception.class)
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        return null;
    }


    private List<Event> checkEvents(List<Long> events) {
        List<Event> resultList = new ArrayList<>();
        for (Long eventId : events) {
            resultList.add(eventRepository.findById(eventId).orElseThrow(() -> new ApiError(HttpStatus.NOT_FOUND.toString(),
                    "The required object was not found.",
                    "Event with id=" + eventId + " was not found",
                    LocalDateTime.now())));
        }
        return resultList;
    }
}
