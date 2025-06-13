package com.retrip.crew.infra.adapter.out.persistence.mysql.query;

import static com.retrip.crew.domain.entity.QCrew.crew;
import static com.retrip.crew.domain.entity.QPost.post;
import static com.retrip.crew.infra.util.PaginationUtils.checkEndPage;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.retrip.crew.application.in.response.PostResponse;
import com.retrip.crew.application.out.repository.PostQueryRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PostQuerydslRepository implements PostQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public Slice<PostResponse> findPosts(UUID crewId, Pageable page, String keyword) {
        List<PostResponse> fetch =
                query.select(
                                Projections.constructor(
                                        PostResponse.class,
                                        post.id,
                                        post.title.value,
                                        post.content.value))
                        .from(post)
                        .join(crew)
                        .on(crew.id.eq(crewId))
                        .where(postTitleContains(keyword))
                        .offset(page.getOffset())
                        .limit(page.getPageSize())
                        .orderBy(post.createdAt.desc())
                        .fetch();

        return checkEndPage(page, fetch);
    }

    @Override
    public Long getPostCount(UUID crewId, String keyword) {
        return query.select(post.count()).from(post).where(postTitleContains(keyword)).fetchOne();
    }

    private BooleanExpression postTitleContains(String title) {
        return title == null ? null : post.title.value.contains(title);
    }
}
