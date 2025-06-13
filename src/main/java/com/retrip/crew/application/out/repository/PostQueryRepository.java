package com.retrip.crew.application.out.repository;

import com.retrip.crew.application.in.response.PostResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import org.springframework.data.domain.Slice;

public interface PostQueryRepository {
    Slice<PostResponse> findPosts(UUID crewId, Pageable page, String keyword);

    Long getPostCount(UUID crewId, String keyword);
}
