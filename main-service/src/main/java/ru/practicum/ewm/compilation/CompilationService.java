package ru.practicum.ewm.compilation;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.models.*;
import ru.practicum.ewm.event.EventMapper;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventShortDto;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationEventRepository compilationEventRepository;

    public List<CompilationDto> getCompilation(boolean pinned, int from, int size) {

        Pageable pageable = PageRequest.of((from / size), size);
        List<Compilation> compilation = compilationRepository.findCompilationByPinned(pinned, pageable)
                .orElse(Collections.emptyList());

        List<CompilationDto> resultList = new ArrayList<>();

        for (Compilation comp : compilation) {
            resultList.add(compilationToDto(comp));
        }
        return resultList;
    }

    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = checkCompilation(compId);
        return compilationToDto(compilation);
    }

    @Transactional(rollbackOn = Exception.class)
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {

        List<Event> eventList = checkEvents(newCompilationDto.getEvents());
        Compilation compilation = new Compilation();

        compilation.setPinned(newCompilationDto.isPinned());
        compilation.setTitle(newCompilationDto.getTitle());
        Compilation savedCompilation = compilationRepository.save(compilation);

        return saveCompilationRequests(newCompilationDto, savedCompilation, eventList);
    }


    @Transactional(rollbackOn = Exception.class)
    public void deleteCompilation(Long compId) {
        checkCompilation(compId);
        List<CompilationEvent> listByDelete = compilationEventRepository.findByCompilationId(compId);
        for (CompilationEvent compEvent : listByDelete) {
            compilationEventRepository.deleteById(compEvent.getId());
        }
        compilationRepository.deleteById(compId);
    }

    @Transactional(rollbackOn = Exception.class)
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {

        Compilation compilation = checkCompilation(compId);
        List<Event> eventList = checkEvents(updateCompilationRequest.getEvents());

        compilation.setPinned(updateCompilationRequest.isPinned());
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        List<CompilationEvent> compilationEventList = compilationEventRepository.findByCompilationId(compId);

        Compilation savedCompilation = compilationRepository.save(compilation);

        for (CompilationEvent compEvent : compilationEventList) {
            compilationEventRepository.deleteById(compEvent.getId());
        }
        return saveCompilationRequests(updateCompilationRequest, savedCompilation, eventList);
    }

    private List<Event> checkEvents(List<Long> events) {
        List<Event> resultList = new ArrayList<>();
        for (Long eventId : events) {
            resultList.add(eventRepository.findById(eventId)
                    .orElseThrow(() -> new EntityNotFoundException("Event with id=" + eventId + " was not found")));
        }
        return resultList;
    }

    private Compilation checkCompilation(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException("Compilation with id=" + compId + " was not found"));
    }

    private CompilationDto compilationToDto(Compilation compilation) {

        List<CompilationEvent> compilationEvent = compilationEventRepository
                .findByCompilationId(compilation.getId());

        List<EventShortDto> events = new ArrayList<>();

        for (CompilationEvent comEvent : compilationEvent) {
            events.add(EventMapper.eventToEventShortDto(eventRepository
                    .findById(comEvent.getEventId())
                    .orElseThrow(() -> new EntityNotFoundException("Event with id=" + comEvent.getEventId() +
                            " was not found"))));
        }

        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.isPinned());
        compilationDto.setEvents(events);

        return compilationDto;
    }

    private CompilationDto saveCompilationRequests(CompilationParent comp, Compilation savedCompilation, List<Event> eventList) {
        for (Long eventId : comp.getEvents()) {
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
}
