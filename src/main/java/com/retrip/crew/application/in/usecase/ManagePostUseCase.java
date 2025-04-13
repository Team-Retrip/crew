package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.CreatePostRequest;
import com.retrip.crew.application.in.request.UpdatePostRequest;
import com.retrip.crew.application.in.response.CreatePostResponse;

import com.retrip.crew.application.in.response.DeletePostResponse;

import com.retrip.crew.application.in.response.PostResponse;
import com.retrip.crew.application.in.response.UpdatePostResponse;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManagePostUseCase {
    CreatePostResponse createPost(UUID crewId, CreatePostRequest request);

    UpdatePostResponse updatePost(UUID crewId, UUID postId, UpdatePostRequest request);

    DeletePostResponse deletePost(UUID crewId, UUID postId, UUID userId);



}
