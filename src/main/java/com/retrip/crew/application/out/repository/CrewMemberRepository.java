package com.retrip.crew.application.out.repository;

import com.retrip.crew.domain.entity.CrewMember;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewMemberRepository extends ReadRepository<CrewMember, UUID> {
    int countByCrewId(UUID crewId);
}
