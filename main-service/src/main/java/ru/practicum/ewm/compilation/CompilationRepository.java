package ru.practicum.ewm.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.compilation.models.Compilation;

import java.util.List;
import java.util.Optional;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("select c from Compilation c " +
            "where c.pinned =? 1")
    Optional<List<Compilation>> findCompilationByPinned(boolean pinned, Pageable pageable);
}
