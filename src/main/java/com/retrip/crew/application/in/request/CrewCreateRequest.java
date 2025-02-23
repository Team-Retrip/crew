package com.retrip.crew.application.in.request;

import com.retrip.crew.domain.entity.Crew;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CrewCreateRequest(
        UUID leader,
        @Size(min = 1, max = 30)
        String title,
        @Size(min = 1, max = 500)
        String description
){

    public Crew to(UUID leader) {
        return Crew.create(this.title, this.description, leader);
    }
}
