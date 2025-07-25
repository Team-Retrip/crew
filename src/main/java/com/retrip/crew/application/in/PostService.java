package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CreatePostRequest;
import com.retrip.crew.application.in.request.PostOrder;
import com.retrip.crew.application.in.request.UpdatePostRequest;
import com.retrip.crew.application.in.response.CreatePostResponse;
import com.retrip.crew.application.in.response.PostResponse;
import com.retrip.crew.application.in.response.UpdatePostResponse;
import com.retrip.crew.application.in.usecase.GetPostUseCase;
import com.retrip.crew.application.in.usecase.ManagePostUseCase;
import com.retrip.crew.application.out.repository.CrewMemberQueryRepository;
import com.retrip.crew.application.out.repository.CrewQueryRepository;
import com.retrip.crew.application.out.repository.PostQueryRepository;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.CrewMember;
import com.retrip.crew.domain.entity.Post;
import com.retrip.crew.domain.exception.CrewMemberNotFoundException;
import com.retrip.crew.domain.exception.CrewNotFoundException;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import com.retrip.crew.infra.util.PaginationUtils;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService implements ManagePostUseCase, GetPostUseCase {
    private final CrewQueryRepository crewQueryRepository;
    private final CrewMemberQueryRepository crewMemberQueryRepository;
    private final PostQueryRepository postQueryRepository;

    @Override
    public CreatePostResponse createPost(UUID crewId, CreatePostRequest request) {
        Crew crew =
                crewQueryRepository
                        .findByIdWithPosts(crewId)
                        .orElseThrow(CrewNotFoundException::new);
        Post post = request.to(crew);
        crew.getPosts().add(post);
        return CreatePostResponse.of(post);
    }

    @Override
    public UpdatePostResponse updatePost(UUID crewId, UUID postId, UpdatePostRequest request) {
        Crew crew =
                crewQueryRepository
                        .findByIdWithPosts(crewId)
                        .orElseThrow(CrewNotFoundException::new);
        Post post = crew.getPosts().updatePost(postId, request.title(), request.content(), request.memberId());
        return UpdatePostResponse.of(post);
    }

    @Override
    public void deletePost(UUID crewId, UUID postId, UUID memberId) {
        Crew crew =
                crewQueryRepository
                        .findByIdWithPosts(crewId)
                        .orElseThrow(CrewNotFoundException::new);
        CrewMember crewMember = findCrewMemberByMemberId(crewId, memberId);
        crew.getPosts().deletePost(postId, crewMember);
    }

    private CrewMember findCrewMemberByMemberId(UUID crewId, UUID memberId) {
        return crewMemberQueryRepository
                .findCrewMemberByMemberId(crewId, memberId)
                .orElseThrow(CrewMemberNotFoundException::new);
    }

    @Override
    public ScrollPageResponse<PostResponse> getPosts(UUID crewId, Pageable page, String keyword, PostOrder order, String sort) {
        Pageable orderPageable =
                PaginationUtils.createPageRequest(page, order.getField(), sort);
        Slice<PostResponse> result = postQueryRepository.findPosts(crewId, orderPageable, keyword);
        Long totalCount = postQueryRepository.getPostCount(crewId, keyword);
        return ScrollPageResponse.of(totalCount, result.hasNext(), result.getContent());
    }
}
