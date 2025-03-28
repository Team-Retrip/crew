package com.retrip.crew.infra.adapter.in.presentation.rest;

import com.retrip.crew.application.in.request.CreatePostRequest;
import com.retrip.crew.application.in.request.UpdatePostRequest;
import com.retrip.crew.application.in.response.CreatePostResponse;
import com.retrip.crew.application.in.response.DeletePostResponse;
import com.retrip.crew.application.in.response.UpdatePostResponse;
import com.retrip.crew.application.in.usecase.ManagePostUseCase;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/crews")
@RestController
@Tag(name = "Post", description = "자유 게시판 서비스")
public class PostController {
    private final ManagePostUseCase managePostUseCase;

    @PostMapping("/{crewId}/posts")
    @Schema(description = "크루 자유 게시판 생성")
    public ApiResponse<CreatePostResponse> createPost(
            @PathVariable UUID crewId,
            @RequestBody CreatePostRequest request
    ) {
        CreatePostResponse post = managePostUseCase.createPost(crewId, request);
        return ApiResponse.created(post);
    }

    @PutMapping("/{crewId}/posts/{postId}")
    @Schema(description = "크루 자유 게시판 수정")
    public ApiResponse<UpdatePostResponse> updatePost(
            @PathVariable UUID crewId,
            @PathVariable UUID postId,
            @RequestBody UpdatePostRequest request
    ) {
        UpdatePostResponse post = managePostUseCase.updatePost(postId, request);
        return ApiResponse.ok(post);
    }

    @DeleteMapping("/{crewId}/posts/{postId}")
    @Schema(description = "크루 자유 게시판 삭제")
    public ApiResponse<DeletePostResponse> deletePost(
            @PathVariable UUID crewId,
            @PathVariable UUID postId,
            @RequestParam UUID userId
    ) {
        DeletePostResponse post = managePostUseCase.deletePost(crewId, postId, userId);
        return ApiResponse.created(post);
    }
}
