package com.retrip.crew.application.in.request;

import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Introduction;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "크루 자기소개 등록 Request")
public record IntroductionCreateRequest(

        @Schema(description = "로그인한 사용자 - 임시 방편")
        UUID loginMemberId,
        @Schema(description = "자기소개 제목")
        String title,
        @Schema(description = "자기소개 본문")
        String content
) {
    public Introduction to(Crew crew){
        return Introduction.create(this.loginMemberId, this.title, this.content, crew);
    }
}
