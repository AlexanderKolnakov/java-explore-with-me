package ru.practicum.mainserver.compilation.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMPILATIONS_EVENTS")
public class CompilationEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "COMPILATION_ID")
    private Long CompilationId;

    @Column(name = "EVENT_ID")
    private Long EventId;

}
