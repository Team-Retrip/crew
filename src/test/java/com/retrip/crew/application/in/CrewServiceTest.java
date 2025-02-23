package com.retrip.crew.application.in;

import com.retrip.crew.application.in.request.CrewCreateRequest;
import com.retrip.crew.application.in.response.CrewCreateResponse;
import com.retrip.crew.application.out.repository.CrewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CrewServiceTest {
    @Autowired
    CrewRepository crewRepository;

    CrewService crewService;
    UUID memberId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        crewService = new CrewService(crewRepository);
    }

    @DisplayName("크루를 생성 한다.")
    @Test
    void createCrew() {
        CrewCreateRequest request = new CrewCreateRequest(
                memberId,
                "속초 크루원 구함",
                "속초 친구 구합니다! 나이는 20~40.. 많은 가입 부탁드립니다."
        );
        CrewCreateResponse response = crewService.createCrew(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.leaderId()).isNotNull();
    }
}
