package com.retrip.crew.application.in.request;

import com.retrip.crew.domain.entity.Crew;
import jakarta.validation.constraints.Min;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CrewCreateRequest(
        UUID leader,
        @Size(min = 1, max = 30)
        String title,
        @Size(min = 1, max = 500)
        String description,
        @Schema(description = "크루 최대 인원")
        @Min(1)
        int maxMembers
){

    public Crew to(UUID leader) {
        return Crew.create(this.title, this.description, this.maxMembers, leader);
    }
}
