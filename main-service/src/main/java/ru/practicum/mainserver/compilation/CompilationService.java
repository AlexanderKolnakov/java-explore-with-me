package ru.practicum.mainserver.compilation;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainserver.compilation.models.CompilationDto;
import ru.practicum.mainserver.compilation.models.NewCompilationDto;
import ru.practicum.mainserver.compilation.models.UpdateCompilationRequest;

import java.util.List;

@Service
@AllArgsConstructor
public class CompilationService {
    public List<CompilationDto> getCompilation(boolean pinned, int from, int size) {
        return null;
    }

    public CompilationDto getCompilationById(int compId) {
        return null;

    }

    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        return null;

    }

    public void deleteCompilation(int compId) {
    }

    public CompilationDto updateCompilation(int compId, UpdateCompilationRequest updateCompilationRequest) {
        return null;
    }
}
