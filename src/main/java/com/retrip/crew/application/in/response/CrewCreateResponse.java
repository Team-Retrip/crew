package com.retrip.crew.application.in.response;

import com.retrip.crew.domain.entity.Crew;

import java.util.UUID;

public record CrewCreateResponse (
        UUID id
){
    public static CrewCreateResponse of(Crew crew){
        return new CrewCreateResponse(
                crew.getId()
        );
    }
}
