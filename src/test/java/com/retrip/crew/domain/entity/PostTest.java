package com.retrip.crew.domain.entity;


import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PostTest {
    @Test
    void 자유_게시판을_생성한다() {
        //given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                5,
                UUID.randomUUID());


        //then
        assertThatCode(() ->
                Post.create(
                        "속초 여행 어디가 좋아요?",
                        "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~",
                        crew)
        ).doesNotThrowAnyException();
    }

    @Test
    void 자유_게시판을_리더는_삭제할_수_있다() {
        //given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                5,
                UUID.randomUUID());
        Post post = Post.create(
                "속초 여행 어디가 좋아요?",
                "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~",
                crew);

        boolean deletable = post.isDeletable(crew.getLeader());

        //then
        assertTrue(deletable);
    }

    @Test
    void 자유_게시판을_수정할_수_있다() {
        //given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                5,
                UUID.randomUUID());
        Post post = Post.create(
                "속초 여행 어디가 좋아요?",
                "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~",
                crew);

        post.update("강릉 여행 어디가 좋아요?", "친구들과 강릉 여행 가려고 합니다. 강릉 여행지 추천해 주세요~",crew.getLeader().getMemberId());

        //then
        assertEquals(post.getTitle().getValue(), "강릉 여행 어디가 좋아요?");
        assertEquals(post.getContent().getValue(), "친구들과 강릉 여행 가려고 합니다. 강릉 여행지 추천해 주세요~");
    }
}
