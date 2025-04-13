package com.retrip.crew.application.in.factory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.retrip.crew.application.in.PostService;
import com.retrip.crew.application.out.repository.*;
import com.retrip.crew.common.config.QuerydslConfig;
import com.retrip.crew.infra.adapter.out.persistence.mysql.query.CrewMemberQuerydslRepository;
import com.retrip.crew.infra.adapter.out.persistence.mysql.query.CrewQuerydslRepository;
import com.retrip.crew.infra.adapter.out.persistence.mysql.query.PostQuerydslRepository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BasePostServiceTest {
    @Autowired protected CrewRepository crewRepository;

    @Autowired protected CrewMemberRepository crewMemberRepository;

    @Autowired JPAQueryFactory jpaQueryFactory;

    protected CrewQueryRepository crewQueryRepository;

    protected CrewMemberQueryRepository crewMemberQueryRepository;
    protected PostQueryRepository postQueryRepository;
    protected PostService postService;

    @BeforeEach
    void setUp() {
        crewQueryRepository = new CrewQuerydslRepository(jpaQueryFactory);
        crewMemberQueryRepository = new CrewMemberQuerydslRepository(jpaQueryFactory);
        postQueryRepository = new PostQuerydslRepository(jpaQueryFactory);
        postService =
                new PostService(
                        crewQueryRepository, crewMemberQueryRepository, postQueryRepository);
    }
}
