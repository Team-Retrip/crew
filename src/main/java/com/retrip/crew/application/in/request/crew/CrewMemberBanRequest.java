package com.retrip.crew.application.in.request.crew;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Schema(description = "크루 회원 차단 Request")
public record CrewMemberBanRequest(

        @Schema(description = "차단할 회원 ID")
        @NotNull
        UUID memberId
) {
}


