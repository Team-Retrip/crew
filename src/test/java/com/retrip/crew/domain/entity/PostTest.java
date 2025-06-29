package com.retrip.crew.domain.entity;


import com.retrip.crew.domain.exception.common.InvalidValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.UUID;

import static com.retrip.crew.common.fixture.CrewFixture.createDefaultQuestions;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {
    @Test
    void 자유_게시판을_생성한다() {
        //given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                5,
                UUID.randomUUID(),
                createDefaultQuestions());


        //then
        assertThatCode(() ->
                Post.create(
                        "속초 여행 어디가 좋아요?",
                        "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~",
                        crew)
        ).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "속초 여행 코스 추천해주세요! 어디가 제일 좋았나요?&친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~",
            "속초 여행 코드 추천&친구들과 오랜만에 시간을 맞춰 속초로 여행을 가려고 해요. 다 함께 즐거운 추억을 만들고 싶은데, 어디를 가야 좋을지 고민 중입니다. 바다도 보고 맛있는 것도 먹고, 예쁜 사진도 많이 찍고 싶어요. 속초에 가보신 분들, 꼭 가봐야 할 명소나 맛집, 숨겨진 핫플레이스가 있다면 추천 좀 부탁드릴게요. 다양한 경험을 들려주시면 여행 계획에 큰 도움이 될 것 같아요!",
    }, delimiter = '&')
    void 자유_게시판_작성에_실패한다(String title, String content) {
        //given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                5,
                UUID.randomUUID(),
                createDefaultQuestions());


        //then
        assertThrows(InvalidValueException.class,
                () -> Post.create(
                        title,
                        content,
                        crew)
        );
    }

    @Test
    void 자유_게시판을_리더는_삭제할_수_있다() {
        //given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                5,
                UUID.randomUUID(),
                createDefaultQuestions());
        Post post = Post.create(
                "속초 여행 어디가 좋아요?",
                "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~",
                crew);

        boolean deletable = post.isDeletable(crew.getLeader());

        //then
        assertTrue(deletable);
    }

    @Test
    void 자유_게시판을_수정한다() {
        //given
        Crew crew = Crew.create(
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다.",
                5,
                UUID.randomUUID(),
                createDefaultQuestions());
        Post post = Post.create(
                "속초 여행 어디가 좋아요?",
                "친구들과 속초 여행 가려고 합니다. 속초 여행지 추천해 주세요~",
                crew);

        post.update("강릉 여행 어디가 좋아요?", "친구들과 강릉 여행 가려고 합니다. 강릉 여행지 추천해 주세요~", crew.getLeader().getMemberId());

        //then
        assertEquals(post.getTitle().getValue(), "강릉 여행 어디가 좋아요?");
        assertEquals(post.getContent().getValue(), "친구들과 강릉 여행 가려고 합니다. 강릉 여행지 추천해 주세요~");
    }
}
