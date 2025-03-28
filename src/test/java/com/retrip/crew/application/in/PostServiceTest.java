package com.retrip.crew.application.in;

import com.retrip.crew.application.in.factory.PostServiceTestFactory;
import com.retrip.crew.application.in.request.CreatePostRequest;
import com.retrip.crew.application.in.request.UpdatePostRequest;
import com.retrip.crew.application.in.response.CreatePostResponse;
import com.retrip.crew.application.in.response.DeletePostResponse;
import com.retrip.crew.application.in.response.UpdatePostResponse;
import com.retrip.crew.common.fixture.PostFixture;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.Post;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostServiceTest extends PostServiceTestFactory {

    @Test
    void 자유_게시글을_작성한다() {

        //given
        Crew crew = crewRepository.save(Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID
        ));
        crewRepository.save(crew);

        CreatePostRequest request = PostFixture.createRequest(
                "속초 여행 어디가 좋아요?",
                "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~"
        );

        //when
        CreatePostResponse response = postService.createPost(crew.getId(), request);

        //then
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isNotNull();
        assertThat(response.content()).isNotNull();
    }
    @Test
    void 자유_게시글을_수정한다() {

        //given
        Crew crew = crewRepository.save(Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID
        ));
        Post post = postRepository.save(
                Post.create(
                        "속초 여행 어디가 좋아요?",
                        "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~",
                        crew)
        );

        UpdatePostRequest request = PostFixture.updateRequest(
                "강릉 여행 어디가 좋아요?",
                "친구들과 강릉 여행 가려고 합니다. 강릉 여행지 추천해 주세요~",
                MEMBER_ID
        );
        //when
        UpdatePostResponse response = postService.updatePost(post.getId(), request);

        //then
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isNotNull();
    }

    @Test
    void 자유_게시글을_삭제한다() {

        //given
        Crew crew = crewRepository.save(Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                100,
                MEMBER_ID
        ));
        Post post = postRepository.save(
                Post.create(
                        "속초 여행 어디가 좋아요?",
                        "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~",
                        crew)
        );


        //when
        DeletePostResponse response = postService.deletePost(crew.getId(), post.getId(), MEMBER_ID);

        //then
        assertThat(response.id()).isNotNull();
    }


}
