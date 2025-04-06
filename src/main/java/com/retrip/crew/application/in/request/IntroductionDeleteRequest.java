package com.retrip.crew.application.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "크루 자기소개 삭제 Request")
public record IntroductionDeleteRequest(

        @Schema(description = "로그인한 사용자 - 임시 방편")
        UUID loginMemberId
) {
}
