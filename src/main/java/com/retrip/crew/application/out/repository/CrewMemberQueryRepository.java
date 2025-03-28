package com.retrip.crew.application.out.repository;

import com.retrip.crew.domain.entity.CrewMember;

import java.util.Optional;
import java.util.UUID;

public interface CrewMemberQueryRepository {
    Optional<CrewMember> findCrewMemberByUserId(UUID crewId, UUID userId);
}
