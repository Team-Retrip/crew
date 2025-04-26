package com.retrip.crew.infra.adapter.out.persistence.mysql.query;

import static com.retrip.crew.domain.entity.QCrew.crew;
import static com.retrip.crew.domain.entity.QIntroduction.introduction;
import static com.retrip.crew.infra.util.PaginationUtils.checkEndPage;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.retrip.crew.application.in.response.IntroductionListResponse;
import com.retrip.crew.application.out.repository.IntroductionQueryRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IntroductionQuerydslRepository implements IntroductionQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Slice<IntroductionListResponse> getIntroductions(UUID crewId, Pageable pageable) {
        List<IntroductionListResponse> fetch = query.select(Projections.constructor(IntroductionListResponse.class,
                                introduction.crew.id.as("crewId"),
                                introduction.id.as("introductionId"),
                                introduction.title.value.as("title"),
                                introduction.createdAt.as("createdAt"),
                                introduction.editedAt.as("editedAt")
                        )
                )
                .from(introduction)
                .join(crew).on(crew.id.eq(introduction.crew.id))
                .where(introduction.crew.id.eq(crewId))
                .limit(pageable.getPageSize() + 1)
                .offset(pageable.getOffset())
                .orderBy(introduction.editedAt.desc())
                .fetch();
        return checkEndPage(pageable, fetch);
    }

    @Override
    public Long getIntroductionCount(UUID crewId) {
        return query.select(introduction.count())
                .from(introduction)
                .where(introduction.crew.id.eq(crewId))
                .fetchOne();
    }
}
