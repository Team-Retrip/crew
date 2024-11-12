package com.retrip.crew.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@Entity
public class Crew {
    @Id
    private final UUID id;
    private String name;

    protected Crew() {
        id = null;
    }
}
