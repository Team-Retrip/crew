package com.retrip.crew.application.in.request.crew;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "크루 멤버 추방 Request")
public record CrewMemberExpelRequest(

        @Schema(description = "추방할 멤버 ID")
        @NotNull
        UUID memberId
) {
}


