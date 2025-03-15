package com.retrip.crew.domain.vo;

import com.retrip.crew.domain.exception.common.InvalidValueException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CrewIntroductionTitleTest {

    @Test
    public void 크루_이름은_30자_미만으로_설정해야한다() {
        assertThatThrownBy(() -> new CrewTitle("통천, 회양, 평강, 이천, 김화, 철원, 양구, 인제, 고성, 강릉, 속초 등 크루 인원 모집합니다."))
                .isExactlyInstanceOf(InvalidValueException.class);
    }
}
