package com.retrip.crew.application.in.request;

import com.retrip.crew.domain.entity.Crew;
import jakarta.validation.constraints.Size;

public record CrewCreateRequest(
        @Size(min = 1, max = 30)
        String title
){

    public Crew to() {
        return Crew.create(this.title);
    }
}
