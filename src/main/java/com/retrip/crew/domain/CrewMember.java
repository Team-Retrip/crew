package com.retrip.crew.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class CrewMember {
    @Id
    private UUID id;
    private String name;
}
