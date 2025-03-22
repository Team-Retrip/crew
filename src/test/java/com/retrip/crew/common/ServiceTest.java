package com.retrip.crew.common;

import com.retrip.crew.application.in.CrewService;
import com.retrip.crew.application.out.repository.CrewMemberRepository;
import com.retrip.crew.application.out.repository.CrewQueryRepository;
import com.retrip.crew.application.out.repository.CrewRepository;
import com.retrip.crew.common.config.QuerydslConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ServiceTest {

    @Autowired
    protected CrewRepository crewRepository;

    @Autowired
    protected CrewMemberRepository crewMemberRepository;

    @Autowired
    protected CrewQueryRepository crewQueryRepository;

    protected CrewService crewService;

    @BeforeEach
    void setUp() {
        crewService = new CrewService(crewRepository, crewMemberRepository, crewQueryRepository);
    }
}
