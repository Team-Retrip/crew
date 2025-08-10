package com.retrip.crew.application.out.repository;

import com.retrip.crew.domain.entity.CrewBanMember;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrewBanMemberRepository extends ReadRepository<CrewBanMember, UUID> {
    List<CrewBanMember> findAllByCrewId(UUID crewId);
}
