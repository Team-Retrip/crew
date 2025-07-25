package com.retrip.crew.application.out.repository;

import com.retrip.crew.domain.entity.CrewMember;
import com.retrip.crew.domain.entity.RecruitmentQuestion;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

public interface RecruitmentQuestionRepository extends ReadRepository<RecruitmentQuestion, UUID> {

    @Query("""
            select rq
            from RecruitmentQuestion rq
            join fetch rq.crew c
            where rq.id = :questionId and c.id = :crewId
           """)
    Optional<RecruitmentQuestion> findByIdAndCrewId(UUID questionId, UUID crewId);
}
