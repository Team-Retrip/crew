package com.retrip.crew.application.in.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "자유 게시글 조회 Response")
public record PostResponse(
        @Schema(description = "자유 게시글 Id") UUID id,
        @Schema(description = "자유 게시글 Title") String title,
        @Schema(description = "자유 게시글 Contents") String Contents) {}
