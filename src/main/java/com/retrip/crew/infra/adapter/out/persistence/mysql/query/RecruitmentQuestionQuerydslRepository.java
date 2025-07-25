package com.retrip.crew.infra.adapter.out.persistence.mysql.query;

import static com.retrip.crew.domain.entity.QRecruitmentQuestion.recruitmentQuestion;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.retrip.crew.application.in.response.RecruitmentQuestionResponse;
import com.retrip.crew.application.out.repository.RecruitmentQuestionQueryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RecruitmentQuestionQuerydslRepository implements RecruitmentQuestionQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public List<RecruitmentQuestionResponse> findRecruitmentQuestions(UUID crewId) {
        return query
                .select(Projections.constructor(
                        RecruitmentQuestionResponse.class,
                        recruitmentQuestion.id.as("id"),
                        recruitmentQuestion.content.value.as("content"))
                )
                .from(recruitmentQuestion)
                .where(recruitmentQuestion.crew.id.eq(crewId))
                .orderBy(recruitmentQuestion.createdAt.desc())
                .fetch();
    }
}