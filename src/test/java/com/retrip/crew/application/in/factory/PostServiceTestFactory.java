package com.retrip.crew.application.in.factory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.retrip.crew.application.in.CrewService;
import com.retrip.crew.application.in.PostService;
import com.retrip.crew.application.out.repository.*;
import com.retrip.crew.common.config.QuerydslConfig;
import com.retrip.crew.infra.adapter.out.persistence.mysql.query.CrewMemberQuerydslRepository;
import com.retrip.crew.infra.adapter.out.persistence.mysql.query.CrewQuerydslRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class PostServiceTestFactory {
    @Autowired
    protected CrewRepository crewRepository;

    @Autowired
    protected CrewMemberRepository crewMemberRepository;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    protected CrewQueryRepository crewQueryRepository;

    protected CrewMemberQueryRepository crewMemberQueryRepository;

    protected CrewService crewService;
    protected PostService postService;

    protected UUID MEMBER_ID = UUID.fromString("13c8ab91-76bc-4f70-93e9-89f1a65dc640");

    @Autowired
    protected PostRepository postRepository;

    @BeforeEach
    void setUp() {
        crewQueryRepository = new CrewQuerydslRepository(jpaQueryFactory);
        crewMemberQueryRepository = new CrewMemberQuerydslRepository(jpaQueryFactory);
        crewService = new CrewService(crewRepository, crewMemberRepository, crewQueryRepository);
        postService = new PostService(crewService, postRepository, crewMemberQueryRepository);
    }
}
