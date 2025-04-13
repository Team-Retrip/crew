package com.retrip.crew.infra.adapter.out.persistence.mysql.query;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.retrip.crew.domain.entity.QCrew.crew;
import static com.retrip.crew.domain.entity.QCrewMember.crewMember;
import static com.retrip.crew.domain.entity.QPost.post;
import static com.retrip.crew.infra.util.PaginationUtils.checkEndPage;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.retrip.crew.application.in.request.CrewOrder;
import com.retrip.crew.application.in.response.CrewListResponse;
import com.retrip.crew.application.out.repository.CrewQueryRepository;
import com.retrip.crew.domain.entity.Crew;
import com.retrip.crew.domain.entity.QCrewMember;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CrewQuerydslRepository implements CrewQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public Slice<CrewListResponse> getCrews(Pageable pageable, String keyword) {
        QCrewMember subCrewMember = new QCrewMember("subCrewMember");
        NumberPath<Long> MemberCountAlias = Expressions.numberPath(Long.class, "memberCount");
        OrderSpecifier<?> orderSpecifier = createCrewOrderSpecifier(pageable);

        List<CrewListResponse> fetch =
                query.select(
                                Projections.constructor(
                                        CrewListResponse.class,
                                        crew.id.as("id"),
                                        crew.title.value.as("title"),
                                        crewMember.id.as("leaderId"),
                                        ExpressionUtils.as(
                                                select(subCrewMember.count())
                                                        .from(subCrewMember)
                                                        .where(subCrewMember.crew.id.eq(crew.id)),
                                                MemberCountAlias),
                                        crew.recruitment.maxMembers.as("maxMemberCount")))
                        .from(crew)
                        .join(crewMember)
                        .on(crewMember.crew.id.eq(crew.id))
                        .where(crewTitleContains(keyword))
                        .limit(pageable.getPageSize() + 1)
                        .offset(pageable.getOffset())
                        .orderBy(orderSpecifier)
                        .fetch();
        return checkEndPage(pageable, fetch);
    }

    @Override
    public Long getCrewCount(String keyword) {
        return query.select(crew.count()).from(crew).where(crewTitleContains(keyword)).fetchOne();
    }

    @Override
    public Optional<Crew> findByIdWithPosts(UUID id) {
        return Optional.ofNullable(
                query.selectFrom(crew)
                        .leftJoin(crew.posts.values, post).fetchJoin()
                        .where(crew.id.eq(crew.id))
                        .fetchOne());
    }

    private BooleanExpression crewTitleContains(String title) {
        return title == null ? null : crew.title.value.contains(title);
    }

    private OrderSpecifier<?> createCrewOrderSpecifier(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                if (CrewOrder.DATE.getField().equals(order.getProperty())) {
                    return new OrderSpecifier<>(direction, crew.createdAt);
                } else {
                    return new OrderSpecifier<>(Order.ASC, crew.createdAt);
                }
            }
        }
        return new OrderSpecifier<>(Order.ASC, crew.createdAt);
    }
}
