package com.retrip.crew.domain.vo;

import com.retrip.crew.domain.exception.CrewTitleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CrewIntroductionTitleTest {

    @Test
    @DisplayName("크루 이름은 30자 미만으로 설정해야한다.")
    public void validateName() {
        assertThatThrownBy(() -> new CrewTitle("통천, 회양, 평강, 이천, 김화, 철원, 양구, 인제, 고성, 강릉, 속초 등 크루 인원 모집합니다.")).isExactlyInstanceOf(CrewTitleException.class);
    }
}
