package com.retrip.crew.infra.adapter.out.persistence.mysql.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.retrip.crew.application.out.repository.CrewMemberQueryRepository;
import com.retrip.crew.domain.entity.CrewMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static com.retrip.crew.domain.entity.QCrew.crew;
import static com.retrip.crew.domain.entity.QCrewMember.crewMember;

@Repository
@RequiredArgsConstructor
public class CrewMemberQuerydslRepository implements CrewMemberQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public Optional<CrewMember> findCrewMemberByUserId(UUID crewId, UUID userId) {
        return Optional.ofNullable(query.select(crewMember)
                .from(crew)
                .join(crewMember).on(crewMember.crew.id.eq(crew.id))
                .where(crewMember.memberId.eq(userId), crew.id.eq(crewId))
                .fetchOne());
    }
}
