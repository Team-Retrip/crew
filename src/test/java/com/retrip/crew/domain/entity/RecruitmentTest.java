package com.retrip.crew.domain.entity;

import com.retrip.crew.domain.exception.common.InvalidValueException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class RecruitmentTest {

    @Test
    void 모집을_생성한다() {
        assertThatCode(() -> new Recruitment(10, List.of("질문1", "질문2")))
                .doesNotThrowAnyException();
    }

    @Test
    void 질문이_10개를_넘으면_예외가_발생한다() {

        List<String> questions = List.of(
                "1","2","3","4","5","6","7","8","9","10","11"
        );

        // when, then
        assertThatThrownBy(() -> new Recruitment(10, questions))
                .isInstanceOf(InvalidValueException.class)
                .hasMessageContaining("질문");
    }

    @Test
    void 질문_내용이_100자를_넘으면_예외가_발생한다() {
        String longQuestion = "a".repeat(101);
        List<String> questions = List.of(longQuestion);

        assertThatThrownBy(() -> new Recruitment(10, questions))
                .isInstanceOf(InvalidValueException.class)
                .hasMessageContaining("질문 내용은 100자를 넘을 수 없습니다.");
    }


    @Test
    void 질문_목록을_문자열로_조회할_수_있다() {
        // given
        List<String> questions = List.of("가입 이유는?", "이전에 크루 활동을 해본 경험이 있나요?");
        Recruitment recruitment = new Recruitment(10, questions);

        // when
        List<String> result = recruitment.getRecruitmentQuestions();

        // then
        assertThat(result).containsExactlyElementsOf(questions);
    }
}
