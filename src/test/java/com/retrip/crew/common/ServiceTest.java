package com.retrip.crew.common;

import com.retrip.crew.application.in.CrewService;
import com.retrip.crew.application.in.DemandService;
import com.retrip.crew.application.out.repository.*;
import com.retrip.crew.common.config.QuerydslConfig;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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

    @Autowired
    protected CrewDemandRepository demandRepository;

    @Autowired
    protected IntroductionRepository introductionRepository;

    @Autowired
    protected IntroductionQueryRepository introductionQueryRepository;

    @Autowired
    protected CrewBanMemberRepository crewBanMemberRepository;

    @Autowired
    protected RecruitmentQuestionQueryRepository recruitmentQuestionQueryRepository;

    @Autowired
    protected RecruitmentQuestionRepository recruitmentQuestionRepository;

    @Autowired
    protected TestEntityManager entityManager;

    protected CrewService crewService;

    protected DemandService demandService;

    @BeforeEach
    void setUp() {
        crewService = new CrewService(crewRepository, crewMemberRepository, crewQueryRepository,  introductionRepository, crewBanMemberRepository, introductionQueryRepository);
        demandService = new DemandService(crewRepository, demandRepository, crewQueryRepository, recruitmentQuestionRepository, recruitmentQuestionQueryRepository);
    }
}
