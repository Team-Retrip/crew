package com.retrip.crew.application.in.usecase;

import com.retrip.crew.application.in.request.CrewOrder;
import com.retrip.crew.application.in.request.PostOrder;
import com.retrip.crew.application.in.response.PostResponse;

import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetPostUseCase {

    ScrollPageResponse<PostResponse> getPosts(UUID crewId, Pageable page, String keyword, PostOrder order, String sort);
}
