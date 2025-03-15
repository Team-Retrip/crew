package com.retrip.crew.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.retrip.crew.infra.adapter.out.persistence.mysql.query.CrewQuerydslRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@TestConfiguration
public class QuerydslConfig {

    @Autowired
    EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public CrewQuerydslRepository crewQuerydslRepository(JPAQueryFactory jpaQueryFactory) {
        return new CrewQuerydslRepository(jpaQueryFactory);
    }
}
