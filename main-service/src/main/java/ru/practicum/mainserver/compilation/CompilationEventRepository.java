package ru.practicum.mainserver.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.mainserver.compilation.models.CompilationEvent;

import java.util.List;

public interface CompilationEventRepository extends JpaRepository<CompilationEvent, Long> {

    @Query("select ce from CompilationEvent ce " +
            "where ce.CompilationId =? 1 ")
    List<CompilationEvent> findByCompilationId(Long id);
}
