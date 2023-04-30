package ru.practicum.mainserver.compilation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.mainserver.compilation.models.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
}
