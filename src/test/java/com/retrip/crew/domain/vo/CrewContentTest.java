package com.retrip.crew.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CrewContentTest {

    @Test
    @DisplayName("크루 이름은 30자 미만으로 설정해야한다.")
    public void validateName() {
        assertThatThrownBy(() -> new CrewContent(
                "통천, 회양, 평강, 이천, 김화, 철원, 양구, 인제, 고성, 강릉, 속초 등 크루 인원 모집합니다. 다들 오세요",
                "강원도 크루 모집합니다.")
        )
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("크루 설명은 100자 미만으로 설정해야한다.")
    public void validateDescription() {
        assertThatThrownBy(() -> new CrewContent(
                "춘천 크루 모집 92년생~76년생",
                "춘천에서 좋은분들과 식사하고 차도 마시며 살아가는 이야기를 나누는 모임입니다. 등산,낚시,골프,당구,배드민턴, 영화등 다양한 취미활동도 같이 하면서 친분을 쌓고 인맥을 넓히며 춘천에 대한 다양한 정보도 공유하고 진솔한 삶의 의미를 찾을수 있도록 인연을 무엇보다 소중하게 생각하는 모임입니다.\n" +
                        "\n" +
                        "지속적으로 모임방이 유지되는 이유는 배려심과 생각이 깊은 많은회원분들이 있기에 춘천의 대표 모임방으로 인정받은게 아닌가 싶습니다. 많은 분들이 오셔서 춘천에서 살아가는 이야기를 나눠 보자구요~\n")
        )
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
