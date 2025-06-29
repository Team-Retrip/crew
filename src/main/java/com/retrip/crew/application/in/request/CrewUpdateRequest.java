package com.retrip.crew.application.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "크루 정보 수정 Request")
public record CrewUpdateRequest(
        @Schema(description = "크루 타이틀")
        @Size(min = 1, max = 30)
        String title,

        @Schema(description = "크루 설명")
        @Size(min = 1, max = 500)
        String description,

        @Schema(description = "크루 최대 인원수")
        @Size(min = 5, max = 1000)
        int maxMembers
) {
}
