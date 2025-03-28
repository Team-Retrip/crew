package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CreatePostRequest;
import com.retrip.crew.application.in.request.UpdatePostRequest;
import com.retrip.crew.application.in.response.CreatePostResponse;
import com.retrip.crew.application.in.response.DeletePostResponse;
import com.retrip.crew.application.in.response.UpdatePostResponse;
import com.retrip.crew.application.in.usecase.ManagePostUseCase;
import com.retrip.crew.application.out.repository.CrewMemberQueryRepository;
import com.retrip.crew.application.out.repository.PostRepository;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.CrewMember;
import com.retrip.crew.domain.entity.Post;
import com.retrip.crew.domain.exception.CrewMemberNotFoundException;
import com.retrip.crew.domain.exception.PostNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService implements ManagePostUseCase {
    private final CrewService crewService;
    private final PostRepository postRepository;
    private final CrewMemberQueryRepository crewMemberQueryRepository;



    @Override
    @Transactional
    public CreatePostResponse createPost(UUID crewId, CreatePostRequest request) {
        Crew crew = crewService.findById(crewId);
        Post post = postRepository.save(request.to(crew));
        return CreatePostResponse.of(post);
    }

    @Override
    @Transactional
    public UpdatePostResponse updatePost(UUID postId, UpdatePostRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.update(request.title(), request.content(), request.userId());
        return UpdatePostResponse.of(post);
    }

    @Override
    @Transactional
    public DeletePostResponse deletePost(UUID crewId, UUID postId, UUID userId) {
        CrewMember crewMember = findCrewMemberByUserId(crewId, userId);
        Post post = findPostById(postId);
        if (post.isDeletable(crewMember)) {
            postRepository.deleteById(postId);
            return DeletePostResponse.of(postId);
        }
        throw new PostNotFoundException();
    }

    private CrewMember findCrewMemberByUserId(UUID crewId, UUID userId) {
        return crewMemberQueryRepository.findCrewMemberByUserId(crewId, userId)
                .orElseThrow(CrewMemberNotFoundException::new);
    }


    private Post findPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }
}
