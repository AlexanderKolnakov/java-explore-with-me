package ru.practicum.ewm.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.models.CompilationEvent;

import java.util.List;

public interface CompilationEventRepository extends JpaRepository<CompilationEvent, Long> {

    @Query("select ce from CompilationEvent ce " +
            "where ce.compilationId =? 1 ")
    List<CompilationEvent> findByCompilationId(Long id);
}
