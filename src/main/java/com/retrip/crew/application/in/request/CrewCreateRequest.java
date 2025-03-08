package com.retrip.crew.application.in.request;

import com.retrip.crew.domain.entity.Crew;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(description = "크루 생성 Request")
public record CrewCreateRequest(
        @Schema(description = "리더 ID")
        UUID leader,

        @Schema(description = "크루 타이틀")
        @Size(min = 1, max = 30)
        String title,

        @Schema(description = "크루 설명")
        @Size(min = 1, max = 500)
        String description,

        @Schema(description = "크루 최대 인원수")
        @Size(min = 5, max = 1000)
        int maxMembers
){

    public Crew to(UUID leader) {
        return Crew.create(this.title, this.description, this.maxMembers, leader);
    }
}
