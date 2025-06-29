package com.retrip.crew.application.in;

import com.retrip.crew.application.in.factory.BasePostServiceTest;
import com.retrip.crew.application.in.request.CreatePostRequest;
import com.retrip.crew.application.in.request.PostOrder;
import com.retrip.crew.application.in.request.UpdatePostRequest;
import com.retrip.crew.application.in.response.CreatePostResponse;
import com.retrip.crew.application.in.response.PostResponse;
import com.retrip.crew.application.in.response.UpdatePostResponse;
import com.retrip.crew.common.fixture.PostFixture;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.infra.adapter.in.presentation.rest.common.ScrollPageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;

import static com.retrip.crew.common.fixture.CrewFixture.createDefaultQuestions;
import static com.retrip.crew.common.fixture.PostFixture.MEMBER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PostServiceTest extends BasePostServiceTest {

    @Test
    void 자유_게시글을_조회한다() {

        // given
        Crew crew =
                crewRepository.save(
                        Crew.create(
                                "속초 크루원 구함",
                                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                                100,
                                MEMBER_ID,
                                createDefaultQuestions()
                                ));
        postService.createPost(
                crew.getId(),
                PostFixture.createRequest("속초 여행 어디가 좋아요?", "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~"));
        postService.createPost(
                crew.getId(),
                PostFixture.createRequest("강릉 여행 어디가 좋아요?", "친구들과 강릉 여행 가려고 합니다. 강릉 여행지 추천해 주세요~"));
        postService.createPost(
                crew.getId(), PostFixture.createRequest("속초 중앙 시장, 오징어 순대", "속초 오징어 순대 맛있나요?"));

        // when
        PageRequest pageable = PageRequest.of(0, 10);
        ScrollPageResponse<PostResponse> response =
                postService.getPosts(crew.getId(), pageable, "속초", PostOrder.DATE, "desc");

        // then
        assertAll(
                "크루 검색 및 정렬 검증",
                () -> assertThat(response).isNotNull(),
                () -> assertThat(response.getList()).hasSize(2),
                () -> assertThat(response.getList().getFirst().title()).isEqualTo("속초 중앙 시장, 오징어 순대"),
                () ->
                        assertThat(response.getList().getFirst().Contents())
                                .isEqualTo("속초 오징어 순대 맛있나요?"),
                () -> assertThat(response.isHasNext()).isFalse());
    }

    @Test
    void 자유_게시글을_작성한다() {

        // given
        Crew crew =
                crewRepository.save(
                        Crew.create(
                                "속초 크루원 구함",
                                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                                100,
                                MEMBER_ID,
                                createDefaultQuestions()));
        crewRepository.save(crew);

        CreatePostRequest request =
                PostFixture.createRequest("속초 여행 어디가 좋아요?", "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~");

        // when
        CreatePostResponse response = postService.createPost(crew.getId(), request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isNotNull();
        assertThat(response.content()).isNotNull();
    }

    @Test
    void 자유_게시글을_수정한다() {
        // given
        Crew crew =
                crewRepository.save(
                        Crew.create(
                                "속초 크루원 구함",
                                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                                100,
                                MEMBER_ID,
                                createDefaultQuestions()));
        CreatePostResponse createPostResponse =
                postService.createPost(
                        crew.getId(),
                        PostFixture.createRequest(
                                "속초 여행 어디가 좋아요?", "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~"));

        UpdatePostRequest request =
                PostFixture.updateRequest(
                        "강릉 여행 어디가 좋아요?", "친구들과 강릉 여행 가려고 합니다. 강릉 여행지 추천해 주세요~", MEMBER_ID);
        // when
        UpdatePostResponse response =
                postService.updatePost(crew.getId(), createPostResponse.id(), request);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isNotNull();
    }

    @Test
    void 자유_게시글을_삭제한다() {

        // given
        Crew crew =
                crewRepository.save(
                        Crew.create(
                                "속초 크루원 구함",
                                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                                100,
                                MEMBER_ID,
                                createDefaultQuestions()));
        CreatePostResponse createPostResponse =
                postService.createPost(
                        crew.getId(),
                        PostFixture.createRequest(
                                "속초 여행 어디가 좋아요?", "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~"));
        ;

        // when

        // then
        assertDoesNotThrow(
                () -> postService.deletePost(crew.getId(), createPostResponse.id(), MEMBER_ID)
        );
    }
}
